package utils

import expressions.monomials.Monomial
import expressions.unit

fun String.isInt(): Boolean { return this.toIntOrNull() != null }


fun String.toMonomial(): Monomial {
    var coefficient = unit()
    val variables: MutableMap<Char, Int> = mutableMapOf()

    val splited = this.split('*')
    splited.forEach {
        if (it.isInt()) coefficient *= it.toInt()
        else {
            variables[it.first()] = (variables[it.first()] ?: 0) + 1
        }
    }
    return Monomial(coefficient to variables)
}

fun varMapToString(varMap: Map<Char, Int>): String {
    var str = ""
    varMap.toSortedMap().forEach { v, d ->
        str += "$v$d"
    }
    return str
}
fun String.toVarMap(): Map<Char, Int> {
    val varMap = mutableMapOf<Char, Int>()
    var degree = ""
    var currLetter: Char = this.first()
    this.forEachIndexed { i, c ->
        if (c.isLetter()) {
            if (i != 0) {
                varMap[currLetter] = degree.toInt()
                degree = ""
            }
            currLetter = c
        }
        else { degree += c }
    }
    varMap[currLetter] = degree.toInt()

    return varMap
}
