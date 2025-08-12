package rules.statements

import console.Clr
import console.coloured
import expressions.Expression
import expressions.InvalidExpression
import rules.Contradiction
import rules.Disjunction
import rules.Rule

class GreaterThan (body: Pair<Char, Expression>) : Statement(body) {
    val expr = body.second

    override fun _simplify(): Rule {
        val sExpr = expr.simplify()
        return if (sExpr !is InvalidExpression) GreaterThan(variable to sExpr) else Contradiction
    }

    override fun contradictsStatement(other: Statement): Boolean? {
        if (!this.expr.isNumber) return false
        return when (other) {
            is EqualsTo    -> other.expr.isNumber && (this.expr == other.expr || other.expr lessThan this.expr)
            else           -> null
        }
    }

    override fun toString() = "$variable > $expr"
    override fun coloured() = "$variable > ".coloured(Clr.INEQUALITY) + expr.coloured()
}

infix fun Char.greaterThan(expr: Expression) = GreaterThan(this to expr)
infix fun Char.greaterThanUnstrictly(expr: Expression) = Disjunction(equalsTo(expr), greaterThan(expr))
