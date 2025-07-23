package rules

import console.Clr
import console.coloured
import expressions.ExpressionPair
import expressions.InvalidExpression
import expressions.simplify
import expressions.substitute
import rules.statements.EqualsTo
import utils.sorted

class ExprNotEqualsTo (override val body: ExpressionPair) : Rule() {
    override fun _simplify(): Rule = body.simplify().sorted().run {
        if (first is InvalidExpression) Contradiction
        else if (first.isNumber && second.isNumber) if (first == second) Contradiction else Tautology
        else ExprNotEqualsTo(this)
    }

    override fun unaryMinus() = ExprEqualsTo(body)

    override fun _contradicts(other: Rule): Boolean? = when (other) {
        is EqualsTo -> ExprEqualsTo(body.substitute(other.variable, other.expr)).simplify() == Tautology
        else        -> null
    }

    override fun toString() = "! EET: ${body.first} = ${body.second}"

    override fun coloured() = "!".coloured(Clr.NOT) + " EET: ${body.first} = ${body.second}".coloured(Clr.EQUALITY)
}