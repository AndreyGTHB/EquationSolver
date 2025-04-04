package expressions

import expressions.monomials.Monomial
import expressions.numerical.Fraction
import utils.toFraction

fun unitMonomial(): Monomial = Monomial(1.toFraction() to mapOf())
fun unitFraction(): Fraction = 1.toFraction()

fun nullFraction(): Fraction = 0.toFraction()
