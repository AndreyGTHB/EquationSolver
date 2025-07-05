package expressions

import expressions.binary.Quotient
import expressions.longs.Product
import expressions.monomials.Monomial
import expressions.number.Rational
import expressions.number.Real
import expressions.number.over
import expressions.number.toRational

fun unit() = Rational(1 to 1, final=true)
fun unitReal() = Real(1 to unit())
fun unitMonomial() = Monomial(mapOf())
fun unitQuotient() = Quotient(unit() to unit())

fun zero() = Rational(0 to 1, final=true)
fun zeroProduct() = Product(listOf(zero()))
fun zeroQuotient() = Quotient(zero() to unit())

fun two() = 2.toRational()
fun three() = 3.toRational()
fun four() = 4.toRational()
fun five() = 5.toRational()

fun xMon() = Monomial(mapOf('x' to unit()), true)
