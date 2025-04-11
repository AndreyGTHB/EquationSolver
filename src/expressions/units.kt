package expressions

import expressions.binary.Quotient
import expressions.monomials.Monomial
import expressions.numerical.Rational
import utils.toFraction

fun unitMonomial(): Monomial = Monomial(1.toFraction() to mapOf())
fun unit(): Rational = 1.toFraction()
fun unitQuotient(): Quotient = Quotient(unit() to unit())

fun zero(): Rational = 0.toFraction()
