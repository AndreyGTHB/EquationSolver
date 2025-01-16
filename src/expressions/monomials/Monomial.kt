package expressions.monomials

import expressions.Expression
import expressions.binary.NumFraction


class Monomial(override var body: Pair<NumFraction, MutableMap<Char, Int>>) : Expression() {
    override fun simplify() { }
}


fun String.toMonomial(): Monomial {
    var coefficient = NumFraction(1 to 1)
    val vrbls: MutableMap<Char, Int> = mutableMapOf()

    val splited = this.split('*')
    splited.forEach {
        if (it.isInt()) coefficient *= it.toInt()
        else {
            vrbls[it.first()] = vrbls.getOrDefault(it.first(), 0) + 1
        }
    }
    return Monomial(coefficient to vrbls)
}

fun String.isInt(): Boolean { return this.toIntOrNull() != null }