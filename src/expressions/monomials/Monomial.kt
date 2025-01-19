package expressions.monomials

import expressions.Expression
import expressions.numerical.NumFraction


data class Monomial(override val body: Pair<NumFraction, MutableMap<Char, Int>>) : Expression() {
    val coefficient
        get() = body.first
    val varMap
        get() = body.second

    override fun simplified(): Expression { return this }
    override fun simplifiedSoftly(): Monomial { return this }

    operator fun unaryMinus(): Monomial {
        return Monomial(-coefficient to varMap)
    }

    operator fun times(other: Monomial): Monomial {
        val newCoefficient = this.coefficient * other.coefficient
        val newVarMap = varMap.toMutableMap()
        for ((k, v) in other.varMap) {
            newVarMap[k] = newVarMap.getOrDefault(k, 0) + v
        }
        return Monomial(newCoefficient to newVarMap)
    }
}