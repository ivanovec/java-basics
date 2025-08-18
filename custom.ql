/**
 * @description Arrayy access with for-loop index where statically know loop bound exceeds array length
 * @kind problem
 * @problem.severity warning
 * @id simpleautomation/indexofbound
 */

import java

from ArrayAccess aa, ForStmt fs,
     Variable itVar, LessThanComparison cond,
     IntegerLiteral upperBoundLit, ArrayCreationExpr arrInit,
     IntegerLiteral arrLit
where
    itVar = fs.getAnIterationVariable() and
    aa.getIndexExpr() instanceof VarAccess and
    aa.getIndexExpr().(VarAccess).getVariable() = itVar and
    cond = fs.getCondition() and
    cond.getLesserOperand() instanceof VarAccess and
    cond.getLesserOperand().(VarAccess).getVariable() = itVar and
    cond.getGreaterOperand() = upperBoundLit and
    aa.getArray() instanceof VarAccess and
    aa.getArray().(VarAccess).getVariable().getInitializer() = arrInit and
    arrInit.getDimension(0) = arrLit and
    arrLit.getIntValue() < upperBoundLit.getIntValue()
select aa,
    "Array access my go out of bounds: loop var '" +
    itVar.getName() +
    "' has upper bound " + upperBoundLit.getValue() +
    " but array length is " + arrLit.getValue() + "."
