package expressions

import expressions.binary.Quotient
import expressions.longs.Product
import expressions.monomials.Monomial
import expressions.number.Rational
import utils.over

fun unit(): Rational = 1 over 1
fun unitMonomial(): Monomial = Monomial(unit() to mapOf())
fun unitQuotient(): Quotient = Quotient(unit() to unit())

fun zero(): Rational = 0 over 1
fun zeroProduct(): Product = Product(listOf(zero()))
