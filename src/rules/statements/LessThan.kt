package rules.statements

import expressions.Expression
import rules.Rule

class LessThan (body: Pair<Char, Expression>) : Statement(body) {
    val expr = body.second
    override fun contradictsStatement(other: Statement): Boolean? {
        TODO("Not yet implemented")
    }

    override fun _simplify(): Rule {
        TODO("Not yet implemented")
    }

    override fun toString(): String {
        TODO("Not yet implemented")
    }

    override fun coloured(): String {
        TODO("Not yet implemented")
    }
}