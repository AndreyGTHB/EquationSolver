package expressions

fun commonFactor(a: Expression, b: Expression): Expression {
    if (!(a.final && b.final)) throw RuntimeException("Extracting a factor from a non-simplified expression")
    if (a.isNullFraction() && b.isNullFraction()) throw RuntimeException("Two zeros")
    if (a.isNullFraction()) return b
    if (b.isNullFraction()) return a
    val cf = a.commonFactor(b) ?: b.commonFactor(a) ?: unit()
    return cf.simplify()
}