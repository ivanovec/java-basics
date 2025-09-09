import com.intellij.psi.*
import com.intellij.psi.tree.*
import com.intellij.psi.util.*
import org.intellij.lang.annotations.Language

@Language("HTML")
val htmlDescription = """
 <html>
 </html>
""".trimIndent()

val arrayIndexOutOfBoundsInspection = localInspection { psiFile, inspection ->
    psiFile.descendantsOfType<PsiMethod>().forEach { method ->
        val arrays = findArrays(method)
        method.descendantsOfType<PsiArrayAccessExpression>().forEach { usage ->
            val arrayRef = usage.arrayExpression as? PsiReferenceExpression ?: return@forEach
            val indexExpr = usage.indexExpression ?: return@forEach
            val arrayName = arrayRef.referenceName ?: return@forEach
            val length = arrays[arrayName] ?: return@forEach

            indexExpr.asIntLiteral()?.let { v ->
                println("is literal $v")
                if (v < 0 || v >= length)
                    inspection.registerProblem(usage, "Index $v is out of bounds for array '$arrayName' of length $length")
                return@forEach
            }

            val idxVar = (indexExpr as? PsiReferenceExpression)?.resolve() as? PsiVariable ?: return@forEach

            val guardInterval = collectGuardsInterval(usage, idxVar, arrayName, length)
            val fallback = lastLiteralBeforeUsage(idxVar, usage)
            val finalInterval = guardInterval.intersect(fallback)

            if (finalInterval.isPossiblyOutOfBounds(length)) {
                inspection.registerProblem(
                    usage,
                    "Potential out-of-bounds access to array '$arrayName[$indexExpr]', inferred index range = $finalInterval, length = $length"
                )
            }
        }
    }
}

fun findArrays(m: PsiMethod) = m.descendantsOfType<PsiLocalVariable>()
    .filter { it.type is PsiArrayType }
    .mapNotNull {
        val len = (it.initializer as? PsiNewExpression)
            ?.arrayDimensions?.getOrNull(0)
            ?.asIntLiteral()
        if (len != null) it.name to len else null
    }.toMap()

data class Interval(val min: Int?, val max: Int?) {
    companion object { val TOP = Interval(null, null) }

    fun intersect(other: Interval) = Interval(
        listOfNotNull(min, other.min).maxOrNull(),
        listOfNotNull(max, other.max).minOrNull()
    )

    fun union(other: Interval): Interval =
        Interval(
            min?.let { m1 -> other.min?.let { m2 -> minOf(m1, m2) } },
            max?.let { m1 -> other.max?.let { m2 -> maxOf(m1, m2) } }
        )

    fun isPossiblyOutOfBounds(len: Int) = (min ?: Int.MIN_VALUE) < 0 || (max ?: Int.MAX_VALUE) >= len

    override fun toString() = "[${min ?: "-∞"}..${max ?: "+∞"}]"
}

fun IElementType.invertOp() = when (this) {
    JavaTokenType.LT -> JavaTokenType.GE
    JavaTokenType.LE -> JavaTokenType.GT
    JavaTokenType.GT -> JavaTokenType.LE
    JavaTokenType.GE -> JavaTokenType.LT
    JavaTokenType.EQ -> JavaTokenType.NE
    JavaTokenType.NE -> JavaTokenType.EQ
    else -> null
}

fun PsiExpression?.asIntLiteral(): Int? = (this as? PsiLiteralExpression)?.value as? Int

fun PsiExpression?.isArrayLengthOf(arrayName: String?) = (this as? PsiReferenceExpression)?.let {
    it.referenceName == "length" && (it.qualifierExpression as? PsiReferenceExpression)?.referenceName == arrayName
} ?: false

fun IElementType.toInterval(v: Int) = when (this) {
    JavaTokenType.LT -> Interval(null, v - 1)
    JavaTokenType.LE -> Interval(null, v)
    JavaTokenType.GT -> Interval(v + 1, null)
    JavaTokenType.GE -> Interval(v, null)
    JavaTokenType.EQ -> Interval(v, v)
    else -> Interval.TOP
}

fun PsiBinaryExpression.binaryToInterval(
    variable: PsiVariable,
    arrayName: String?,
    concreteLength: Int?,
    invert: Boolean
): Interval {
    val op = operationTokenType.invertOp()?.takeIf { invert } ?: operationTokenType
    val rightExpr = rOperand ?: return Interval.TOP

    fun valueOf(expr: PsiExpression) =
        expr.asIntLiteral() ?: if (expr.isArrayLengthOf(arrayName)) concreteLength else null

    return when (variable) {
        (lOperand as? PsiReferenceExpression)?.resolve() -> valueOf(rightExpr)?.let { op.toInterval(it) } ?: Interval.TOP
        (rightExpr as? PsiReferenceExpression)?.resolve() -> valueOf(lOperand)?.let {
            op.invertOp()?.toInterval(it) ?: Interval.TOP
        } ?: Interval.TOP
        else -> Interval.TOP
    }
}

fun extractIntervalFromCondition(
    expr: PsiExpression?,
    variable: PsiVariable,
    arrayName: String?,
    concreteLength: Int?,
    invert: Boolean
): Interval = when (expr) {
    null -> Interval.TOP

    is PsiPolyadicExpression -> when (expr.operationTokenType) {
        JavaTokenType.ANDAND ->
            expr.operands
                .map { extractIntervalFromCondition(it, variable, arrayName, concreteLength, invert) }
                .fold(Interval.TOP, Interval::intersect)

        JavaTokenType.OROR ->
            expr.operands
                .map { extractIntervalFromCondition(it, variable, arrayName, concreteLength, invert) }
                .fold(Interval(null, null), Interval::union)

        else -> if (expr is PsiBinaryExpression)
            expr.binaryToInterval(variable, arrayName, concreteLength, invert)
        else Interval.TOP
    }
    else -> Interval.TOP
}


fun collectGuardsInterval(
    usage: PsiArrayAccessExpression,
    variable: PsiVariable,
    arrayName: String?,
    concreteLength: Int?
): Interval {
    println("collectGuardsInterval: ${usage.arrayExpression}")
    var node: PsiElement? = usage.parent
    var acc = Interval.TOP
    while (node != null) {
        when (node) {
            is PsiIfStatement -> {
                val (inThen, inElse) = node.branchContains(usage)
                if (inThen || inElse) {
                    acc = acc.intersect(extractIntervalFromCondition(node.condition, variable, arrayName, concreteLength, inElse))
                }
            }
            is PsiForStatement -> {
                acc = acc.intersect(node.conditionInterval(usage, variable, arrayName, concreteLength))

                when (val init = node.initialization) {
                    is PsiDeclarationStatement -> {
                        init.declaredElements.filterIsInstance<PsiLocalVariable>()
                            .firstOrNull { it.name == variable.name }?.initializer?.asIntLiteral()
                            ?.let { acc = acc.intersect(Interval(it, null)) }
                    }
                    is PsiAssignmentExpression -> {
                        (init.lExpression as? PsiReferenceExpression)?.resolve()
                            .takeIf { it == variable }?.let { init.rExpression?.asIntLiteral()?.let { v -> acc = acc.intersect(Interval(v, null)) } }
                    }
                }
            }
            is PsiWhileStatement, is PsiDoWhileStatement -> {
                acc = acc.intersect((node as PsiLoopStatement).conditionInterval(usage, variable, arrayName, concreteLength))
            }
        }
        node = node.parent
    }
    return acc
}

fun lastLiteralBeforeUsage(variable: PsiVariable?, usage: PsiArrayAccessExpression): Interval {
    if (variable == null) return Interval.TOP
    var cur: PsiElement? = usage
    while (cur != null) {
        val parent = cur.parent
        if (parent is PsiCodeBlock) {
            for (sib in parent.children) {
                if (sib === cur) break
                when (sib) {
                    is PsiDeclarationStatement -> {
                        sib.declaredElements.filterIsInstance<PsiLocalVariable>()
                            .firstOrNull { it == variable }?.initializer?.asIntLiteral()?.let { return Interval(it, it) }
                    }
                    is PsiExpressionStatement -> {
                        (sib.expression as? PsiAssignmentExpression)?.takeIf {
                            (it.lExpression as? PsiReferenceExpression)?.resolve() == variable
                        }?.rExpression?.asIntLiteral()?.let { return Interval(it, it) }
                    }
                }
            }
        }
        cur = parent
    }
    return Interval.TOP
}

fun PsiElement.isAncestorOf(child: PsiElement) = PsiTreeUtil.isAncestor(this, child, true)

fun PsiIfStatement.branchContains(usage: PsiElement) =
    (thenBranch?.isAncestorOf(usage) == true) to (elseBranch?.isAncestorOf(usage) == true)

fun PsiLoopStatement.conditionInterval(
    usage: PsiElement,
    variable: PsiVariable,
    arrayName: String?,
    concreteLength: Int?,
    invert: Boolean = false
): Interval {
    val cond = when (this) {
        is PsiForStatement -> condition
        is PsiWhileStatement -> condition
        is PsiDoWhileStatement -> condition
        else -> null
    }
    return if (cond != null && body?.isAncestorOf(usage) == true)
        extractIntervalFromCondition(cond, variable, arrayName, concreteLength, invert)
    else Interval.TOP
}

listOf(
    InspectionKts(
        id = "ArrayIndexOutOfBoundsInspection",
        localTool = arrayIndexOutOfBoundsInspection,
        name = "Array Index Out of Bounds (simplified guards & intervals)",
        htmlDescription = htmlDescription,
        level = HighlightDisplayLevel.WARNING,
    )
)
