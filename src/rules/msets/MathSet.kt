package rules.msets

import console.Colourable
import expressions.Expression

abstract class MathSet : Colourable {
    abstract operator fun contains(expr: Expression): Boolean
    abstract operator fun contains(other: MathSet): Boolean
    abstract operator fun times(other: MathSet): MathSet

    override fun toString() = this::class.simpleName!!
}