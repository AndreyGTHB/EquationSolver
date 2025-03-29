package expressions

import expressions.monomials.Monomial
import expressions.numerical.Fraction
import utils.toFraction

fun unitMonomial(): Monomial = Monomial(1.toFraction() to mapOf<Char, Int>())
fun unitFraction(): Fraction = 1.toFraction()

