package expressions

import expressions.binary.Quotient
import expressions.longs.Product
import expressions.monomials.Monomial
import expressions.number.Real
import expressions.number.toRational

fun one() = 1.toRational()
fun unitReal() = Real(1 to one())
fun unitMonomial() = Monomial(mapOf())
fun unitQuotient() = Quotient(one() to one())

fun zero() = 0.toRational()
fun zeroProduct() = Product(listOf(zero()))
fun zeroQuotient() = Quotient(zero() to one())

fun two() = 2.toRational()
fun three() = 3.toRational()
fun four() = 4.toRational()
fun five() = 5.toRational()

fun xMon() = Monomial(mapOf('x' to one()), true)
