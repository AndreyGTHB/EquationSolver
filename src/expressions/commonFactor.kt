package expressions

import utils.toFraction

fun commonFactor(a: Expression, b: Expression): Expression {
    if (!(a.final && b.final)) throw RuntimeException("Extracting a factor from a non-simplified expression")
    val cf = a.commonFactor(b) ?: b.commonFactor(a) ?: 1.toFraction()
    return cf.simplify()
}