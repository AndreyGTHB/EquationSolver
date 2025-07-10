package rules

import console.Clr
import console.coloured

object Contradiction : Rule() {
    override val body = null

    override fun simplify() = this
    override fun _intersect(other: Rule) = this
    override fun _union(other: Rule) = other
    override fun unaryMinus() = Tautology

    override fun toString() = "Contradiction"
    override fun coloured() = "Contradiction".coloured(Clr.CONTRADICTION)
}