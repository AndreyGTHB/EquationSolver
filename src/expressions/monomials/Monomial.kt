package expressions.monomials

import expressions.Expression
import expressions.numerical.NumFraction
import utils.isInt


class Monomial(private var _body: Pair<NumFraction, MutableMap<Char, Int>>) : Expression() {
    override val body
        get() = _body
    override fun simplified(): Expression { return this }
    override fun simplifyBody() {  }
}


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