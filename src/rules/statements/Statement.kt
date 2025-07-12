package rules.statements

import rules.Rule

abstract class Statement (val variable: Char, predicate: Any) : Rule() {
    constructor(body: Pair<Char, Any>) : this(body.first, body.second)

    override val body = variable to predicate

    override fun unaryMinus(): Rule = Not(this)

    protected abstract fun _contradicts(other: Statement): Boolean?
    infix fun contradicts(other: Statement): Boolean {
        if (this.variable != other.variable) return false
        return this._contradicts(other) ?: other._contradicts(this) ?: false
    }
}