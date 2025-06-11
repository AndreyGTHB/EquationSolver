package expressions

fun commonFactor(a: Expression, b: Expression): Expression {
    if (!(a.final && b.final)) TODO("Extracting a factor from a non-simplified expression")
    if (a.isZeroRational() && b.isZeroRational()) TODO("Two zeros")
    if (a.isZeroRational()) return b
    if (b.isZeroRational()) return a
    val cf = a.commonFactor(b) ?: b.commonFactor(a) ?: unit()
    return cf.simplify()
}