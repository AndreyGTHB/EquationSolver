package statements

import console.Clr
import console.colouredUnder
import console.toStringUnder

abstract class LongStatement (override val body: Set<Statement>) : Statement() {
    protected fun simplifyBody() = body.map { it.simplify() }.toSet()

    override fun toString() = body.toStringUnder(this::class.simpleName!!)
    override fun coloured() = body.colouredUnder(this::class.simpleName!!, Clr.LONG_STATEMENT)
}