package rules.statements

import console.Clr
import console.coloured
import expressions.Expression
import expressions.InvalidExpression
import rules.Contradiction
import rules.Rule

class EqualsTo(variable: Char, val expr: Expression) : Statement(variable, expr) {
    override fun _simplify(): Rule {
        return when (val it = expr.simplify()) {
            is InvalidExpression   -> Contradiction
            else                   -> EqualsTo(variable, it)
        }
    }

    override fun unaryMinus() = variable notEqualsTo expr

    override fun _1contradicts(other: Statement) = when (other) {
        is Not if other.statement is EqualsTo -> this.expr == other.statement.expr
        is EqualsTo -> this.expr.isNumber && other.expr.isNumber && this.expr != other.expr
        else        -> null
    }

    override fun toString() = "$variable = $expr"
    override fun coloured() = toString().coloured(Clr.EQUALITY)
}

infix fun Char.equalsTo(expr: Expression) = EqualsTo(this, expr)
infix fun Char.notEqualsTo(expr: Expression) = Not(EqualsTo(this, expr))
