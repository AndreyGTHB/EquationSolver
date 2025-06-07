package expressions

import expressions.binary.Quotient
import expressions.longs.Product
import expressions.monomials.Monomial
import expressions.number.Rational
import expressions.number.Real
import utils.over

fun unit(): Rational = 1 over 1
fun unitReal() = Real(1 to unit())
fun unitMonomial() = Monomial(mapOf())
fun unitQuotient() = Quotient(unit() to unit())

fun zero() = 0 over 1
fun zeroProduct() = Product(listOf(zero()))
