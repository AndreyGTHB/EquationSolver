package rules.statements

import console.Clr
import console.coloured
import expressions.Expression
import expressions.InvalidExpression
import expressions.UniversalExpression
import rules.Contradiction
import rules.Rule
import rules.Tautology

class NotEqualsTo (variable: Char, expr: Expression) : Statement(variable to expr) {
    override fun simplify(): Rule {
        expr.simplify().let {
            return when (it) {
                is InvalidExpression   -> Tautology
                is UniversalExpression -> Contradiction
                else                   -> NotEqualsTo(variable, it)
            }
        }
    }

    override fun unaryMinus() = variable equalsTo expr

    override fun _contradicts(other: Statement) = other is EqualsTo && other.expr == this.expr

    override fun toString() = "$variable != $expr"
    override fun coloured() = toString().coloured(Clr.EQUALITY)
}

infix fun Char.notEqualsTo(expr: Expression) = NotEqualsTo(this, expr)
