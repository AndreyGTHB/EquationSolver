package rules.msets

import console.Clr
import console.coloured
import expressions.Expression

object EmptySet : MathSet() {
    override fun contains(expr: Expression) = false
    override fun times(other: MathSet) = this

    override fun coloured() = toString().coloured(Clr.CONTRADICTION)
}
