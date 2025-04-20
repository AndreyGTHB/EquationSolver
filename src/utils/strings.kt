package utils

import expressions.monomials.Monomial
import expressions.numerical.Rational
import expressions.unit
import expressions.zero

fun String.isInt(): Boolean = this.toIntOrNull() != null
fun String.isRational(): Boolean = this.toRationalOrNull() != null


fun String.toRationalOrNull(): Rational? {
    val splitted = this.split('/')
    if (!(splitted.size == 2 && splitted[0].isInt() && splitted[1].isInt())) return null
    val (numer, denom) = splitted.map { it.toInt() }
    return Rational(numer to denom)
}
fun String.toRational(): Rational = this.toRationalOrNull()!!

fun String.toMonomial(): Monomial {
    var coeff = unit()
    val variables: MutableMap<Char, Rational> = mutableMapOf()

    val splitted = this.split('*')
    splitted.forEach {
        if (it.isRational()) coeff *= it.toRational()
        else if (it.isInt())      coeff *= it.toInt()
        else {
            variables[it.first()] = (variables[it.first()] ?: zero()) + 1
        }
    }
    return Monomial(coeff to variables)
}

fun varMapToString(varMap: Map<Char, Rational>): String {
    var str = ""
    varMap.toSortedMap().forEach { (v, d) ->
        str += "$v$d"
    }
    return str
}
fun String.toVarMap(): Map<Char, Rational> {
    val varMap = mutableMapOf<Char, Rational>()
    var degree = ""
    var currLetter: Char = this.first()
    this.forEachIndexed { i, c ->
        if (c.isLetter()) {
            if (i != 0) {
                varMap[currLetter] = degree.toRational()
                degree = ""
            }
            currLetter = c
        }
        else { degree += c }
    }
    varMap[currLetter] = degree.toRational()

    return varMap
}
