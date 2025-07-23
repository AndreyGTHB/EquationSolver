package rules.statements

import rules.Rule

abstract class Statement (val variable: Char, predicate: Any) : Rule() {
    constructor(body: Pair<Char, Any>) : this(body.first, body.second)

    override val body = variable to predicate

    override fun unaryMinus(): Rule = Not(this)

    protected abstract fun _1contradicts(other: Statement): Boolean?
    override fun _contradicts(other: Rule): Boolean? {
        return if (other !is Statement) null
          else if (this.variable != other.variable) false
          else _1contradicts(other)
    }
}