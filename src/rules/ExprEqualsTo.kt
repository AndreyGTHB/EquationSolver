package rules

import console.Clr
import console.coloured
import expressions.ExpressionPair
import expressions.InvalidExpression
import expressions.simplify
import expressions.substitute
import rules.statements.EqualsTo
import utils.sorted

class ExprEqualsTo (override val body: ExpressionPair) : Rule() {
    override fun _simplify(): Rule = body.simplify().sorted().run {
        if (first is InvalidExpression) Contradiction
        else if (first.isNumber && second.isNumber) if (first == second) Tautology else Contradiction
        else ExprEqualsTo(this)
    }

    override fun _contradicts(other: Rule): Boolean? = when (other) {
        is EqualsTo -> ExprEqualsTo(body.substitute(other.variable, other.expr)).simplify() == Contradiction
        else        -> null
    }

    override fun toString() = "EET: (${body.first} = ${body.second})"

    override fun coloured() = toString().coloured(Clr.EQUALITY)
}