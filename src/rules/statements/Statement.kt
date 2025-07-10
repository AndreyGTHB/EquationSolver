package rules.statements

import expressions.Expression
import rules.Rule

abstract class Statement (final override val body: Pair<Char, Expression>) : Rule() {
    val variable = body.first
    val expr = body.second

    protected abstract fun _contradicts(other: Statement): Boolean
    infix fun contradicts(other: Statement): Boolean {
        return this.variable == other.variable && _contradicts(other)
    }
}