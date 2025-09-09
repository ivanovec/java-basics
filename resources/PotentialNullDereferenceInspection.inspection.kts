import com.intellij.analysis.AnalysisScope
import com.intellij.codeInspection.*
import com.intellij.psi.*
import com.intellij.psi.util.PsiTreeUtil

class PotentialNullDereferenceInspection : GlobalInspectionTool() {

    override fun runInspection(
        scope: AnalysisScope,
        manager: InspectionManager,
        globalContext: GlobalInspectionContext,
        problemDescriptionsProcessor: ProblemDescriptionsProcessor
    ) {
        scope.accept(object : JavaRecursiveElementVisitor() {
            override fun visitFile(file: PsiFile) {
                if (file !is PsiJavaFile) return
                val holder = ProblemsHolder(manager, file, false)

                file.accept(object : JavaRecursiveElementVisitor() {
                    override fun visitMethodCallExpression(expression: PsiMethodCallExpression) {
                        val qualifier = expression.methodExpression.qualifierExpression as? PsiReferenceExpression
                        val resolved = qualifier?.resolve()
                        if (resolved is PsiLocalVariable) {
                            val method = PsiTreeUtil.getParentOfType(expression, PsiMethod::class.java)
                            if (method != null && mayBeNullAt(expression, resolved, method)) {
                                holder.registerProblem(
                                    expression.methodExpression,
                                    "Potential NullPointerException: '${resolved.name}' can be null here"
                                )
                            }
                        }
                        super.visitMethodCallExpression(expression)
                    }
                })

                if (holder.hasResults()) {
                    globalContext.refManager.getReference(file)?.let {
                        problemDescriptionsProcessor.addProblemElement(
                            it,
                            *holder.results.toTypedArray()
                        )
                    }
                }
            }
        })
    }

    private fun inferReturnNullness(method: PsiMethod, visited: MutableSet<PsiMethod> = mutableSetOf()): Nullness {
        if (!visited.add(method)) return Nullness.MAYBE_NULL
        val body = method.body ?: return Nullness.MAYBE_NULL
        val returnValues = PsiTreeUtil.findChildrenOfType(body, PsiReturnStatement::class.java)
            .mapNotNull { it.returnValue }
        if (returnValues.isEmpty()) return Nullness.MAYBE_NULL

        return returnValues.map { returnExpr ->
            when (returnExpr) {
                is PsiLiteralExpression ->
                    if (returnExpr.value == null) Nullness.DEFINITELY_NULL else Nullness.DEFINITELY_NOT_NULL

                is PsiMethodCallExpression ->
                    returnExpr.resolveMethod()?.let { inferReturnNullness(it, visited) } ?: Nullness.MAYBE_NULL

                is PsiReferenceExpression -> {
                    val resolved = returnExpr.resolve() as? PsiLocalVariable ?: return@map Nullness.MAYBE_NULL
                    val enclosingMethod = PsiTreeUtil.getParentOfType(returnExpr, PsiMethod::class.java)
                        ?: return@map Nullness.MAYBE_NULL
                    analyzeAssignmentsTo(returnExpr, resolved, enclosingMethod)
                        .lastOrNull()?.second ?: Nullness.MAYBE_NULL
                }
                else -> Nullness.MAYBE_NULL
            }
        }.reduce(Nullness::mergeWith)
    }

    private fun mayBeNullAt(
        call: PsiMethodCallExpression,
        variable: PsiLocalVariable,
        method: PsiMethod
    ): Boolean =
        analyzeAssignmentsTo(call, variable, method, includeInitializer = false)
            .mapNotNull { (expr, nullness) -> (expr as? PsiMethodCallExpression)?.let { it to nullness } }
            .any { (_, nullness) -> nullness == Nullness.DEFINITELY_NULL || nullness == Nullness.MAYBE_NULL }

    private fun analyzeAssignmentsTo(
        usage: PsiElement,
        variable: PsiLocalVariable,
        method: PsiMethod,
        includeInitializer: Boolean = true,
        onlyMethodCalls: Boolean = false
    ): List<Pair<PsiExpression, Nullness>> {
        val body = method.body ?: return emptyList()
        return buildList {
            for (statement in body.statements) {
                if (statement.textOffset >= usage.textOffset) break

                when (statement) {
                    is PsiDeclarationStatement -> {
                        if (!includeInitializer) continue

                        statement.declaredElements
                            .filterIsInstance<PsiLocalVariable>()
                            .find { it.isEquivalentTo(variable) }
                            ?.initializer
                            ?.takeIf { !onlyMethodCalls || it is PsiMethodCallExpression }
                            ?.let { add(it to getNullnessOfExpression(it)) }
                    }

                    is PsiExpressionStatement -> {
                        val assignment = statement.expression as? PsiAssignmentExpression ?: continue
                        val lExpr = assignment.lExpression as? PsiReferenceExpression ?: continue
                        if (lExpr.resolve() != variable) continue

                        assignment.rExpression
                            ?.takeIf { !onlyMethodCalls || it is PsiMethodCallExpression }
                            ?.let { add(it to getNullnessOfExpression(it)) }
                    }
                }
            }
        }
    }

    private fun getNullnessOfExpression(expr: PsiExpression?): Nullness = when (expr) {
        is PsiLiteralExpression -> if (expr.value == null) Nullness.DEFINITELY_NULL else Nullness.DEFINITELY_NOT_NULL
        is PsiMethodCallExpression -> expr.resolveMethod()?.let { inferReturnNullness(it) } ?: Nullness.MAYBE_NULL
        else -> Nullness.MAYBE_NULL
    }

    override fun isGlobalSimpleInspectionTool(): Boolean = true
}

enum class Nullness {
    DEFINITELY_NULL,
    DEFINITELY_NOT_NULL,
    MAYBE_NULL;

    fun mergeWith(other: Nullness): Nullness =
        if (this == other) this else MAYBE_NULL

}

listOf(
    InspectionKts(
        id = "NullDereferenceInspection",
        globalTool = PotentialNullDereferenceInspection(),
        name = "Null dereference check",
        htmlDescription = "Detects dereference of possibly null return values",
        level = HighlightDisplayLevel.WARNING
    )
)