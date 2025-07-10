package statements

import console.Clr
import console.colouredUnder
import console.toStringUnder

abstract class LongRule (override val body: Set<Rule>) : Rule() {
    protected fun simplifyBody() = body.map { it.simplify() }.toSet()

    override fun toString() = body.toStringUnder(this::class.simpleName!!)
    override fun coloured() = body.colouredUnder(this::class.simpleName!!, Clr.LONG_STATEMENT)
}