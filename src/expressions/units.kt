package expressions

import expressions.binary.Quotient
import expressions.longs.Product
import expressions.monomials.Monomial
import expressions.number.Rational
import expressions.number.Real
import expressions.number.over

fun unit() = Rational(1 to 1, final=true)
fun unitReal() = Real(1 to unit())
fun unitMonomial() = Monomial(mapOf())
fun unitQuotient() = Quotient(unit() to unit())

fun zero() = Rational(0 to 1, final=true)
fun zeroProduct() = Product(listOf(zero()))
fun zeroQuotient() = Quotient(zero() to unit())

fun two() = Rational(2 to 1, final=true)
fun three() = Rational(3 to 1, final=true)
fun four() = Rational(4 to 1, final=true)
fun five() = Rational(5 to 1, final=true)
