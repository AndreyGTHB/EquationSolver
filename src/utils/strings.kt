package utils

import expressions.monomials.Monomial
import expressions.numerical.NumFraction

fun String.isInt(): Boolean { return this.toIntOrNull() != null }


fun String.toMonomial(): Monomial {
    var coefficient = NumFraction(1 to 1)
    val variables: MutableMap<Char, Int> = mutableMapOf()

    val splited = this.split('*')
    splited.forEach {
        if (it.isInt()) coefficient *= it.toInt()
        else {
            variables[it.first()] = variables.getOrDefault(it.first(), 0) + 1
        }
    }
    return Monomial(coefficient to variables)
}