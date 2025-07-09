package statements

import console.Clr
import console.coloured

object Tautology : Statement() {
    override val body = null

    override fun simplify() = this
    override fun _intersect(other: Statement) = other
    override fun unaryMinus() = Contradiction

    override fun toString() = "Tautology"
    override fun coloured() = "Tautology".coloured(Clr.TAUTOLOGY)
}