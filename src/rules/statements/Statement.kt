package rules.statements

import rules.Complement
import rules.Rule

abstract class Statement (override val body: Pair<Char, Any>) : Rule() {
    val variable = body.first

    override fun _contradicts(other: Rule) = when (other) {
        is Statement -> {
            if (this.variable == other.variable) contradictsStatement(other) else false
        }
        is Complement if other.body is Statement -> {
            if (this.variable == other.body.variable) impliesStatement(other.body) else false
        }
        else -> null
    }

    protected abstract fun _contradictsStatement(other: Statement): Boolean?
    private fun contradictsStatement(other: Statement): Boolean {
        return this._contradictsStatement(other) ?: other._contradictsStatement(this) ?: false
    }

    protected open fun impliesStatement(other: Statement) = this == other
}