package rules

import console.Clr
import console.coloured

object Tautology : Rule() {
    override val body = null

    override fun _simplify() = this
    override fun _intersect(other: Rule) = other
    override fun _union(other: Rule) = this
    override fun unaryMinus() = Contradiction

    override fun toString() = "Tautology"
    override fun coloured() = "Tautology".coloured(Clr.TAUTOLOGY)
}