package statements

import console.Clr
import console.coloured

object Contradiction : Statement() {
    override val body = null

    override fun simplify() = this
    override fun _intersect(other: Statement) = this
    override fun unaryMinus() = Tautology
    override fun toString() = "Contradiction"
    override fun coloured() = "Contradiction".coloured(Clr.CONTRADICTION)
}