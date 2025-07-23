package rules.msets

import console.Clr
import console.coloured
import expressions.Expression

object Universe : MathSet() {
    override fun contains(expr: Expression) = true
    override fun contains(other: MathSet) = true
    override fun times(other: MathSet) = other

    override fun coloured() = toString().coloured(Clr.TAUTOLOGY)
}