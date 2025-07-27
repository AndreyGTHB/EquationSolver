package rules.statements

import console.Clr
import console.coloured
import expressions.Expression
import expressions.InvalidExpression
import rules.Contradiction
import rules.Rule

class MoreThan (body: Pair<Char, Expression>) : Statement(body) {
    val expr = body.second

    override fun _simplify(): Rule {
        val sExpr = expr.simplify()
        return if (sExpr !is InvalidExpression) MoreThan(variable to sExpr) else Contradiction
    }

    override fun contradictsStatement(other: Statement) = when (other) {
        is EqualsTo
    }

    override fun toString() = "$variable > $expr"

    override fun coloured() = "$variable > ".coloured(Clr.INEQUALITY) + expr.coloured()

}