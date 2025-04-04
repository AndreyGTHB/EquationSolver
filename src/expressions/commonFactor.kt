package expressions

import expressions.numerical.Fraction

fun commonFactor(a: Expression, b: Expression): Expression {
    if (!(a.final && b.final)) throw RuntimeException("Extracting a factor from a non-simplified expression")
    if (a is Fraction && a.isNull() && b is Fraction && b.isNull()) throw RuntimeException("Two zeros")
    if (a is Fraction && a.isNull()) return b
    if (b is Fraction && b.isNull()) return a
    val cf = a.commonFactor(b) ?: b.commonFactor(a) ?: unitFraction()
    return cf.simplify()
}