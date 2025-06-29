package expressions

import expressions.binary.Power
import expressions.binary.Quotient
import expressions.longs.Product
import expressions.longs.Sum
import expressions.monomials.Monomial
import expressions.number.Rational
import expressions.number.Real

val exprOrderMap = mapOf(
    Rational::class to 1,
    Real::class to 2,
    Monomial::class to 3,
    Product::class to 4,
    Power::class to 5,
    Quotient::class to 6,
    Sum::class to 7
)

internal fun compareExpressionTypes(a: Expression, b: Expression): Int {
    return if (a.isNumber() && !b.isNumber()) -1
      else if (!a.isNumber() && b.isNumber())  1
      else                                    exprOrderMap[a::class]!! - exprOrderMap[b::class]!!
//      else                                    a.toString() compareTo b.toString()
}