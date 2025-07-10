package rules.statements

import console.Clr
import console.coloured
import expressions.Expression
import expressions.InvalidExpression
import expressions.UniversalExpression
import rules.Contradiction
import rules.Rule
import rules.Tautology

class EqualsTo(variable: Char, expr: Expression) : Statement(variable to expr) {
    override fun simplify(): Rule {
        expr.simplify().let {
            return when (it) {
                is InvalidExpression   -> Contradiction
                is UniversalExpression -> Tautology
                else                   -> EqualsTo(variable, it)
            }
        }
    }

    override fun unaryMinus() = variable notEqualsTo expr

    override fun _contradicts(other: Statement) = other is NotEqualsTo && other.expr == this.expr

    override fun toString() = "$variable = $expr"
    override fun coloured() = toString().coloured(Clr.EQUALITY)
}

infix fun Char.equalsTo(expr: Expression) = EqualsTo(this, expr)
