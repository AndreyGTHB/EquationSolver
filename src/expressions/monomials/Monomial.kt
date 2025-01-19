package expressions.monomials

import expressions.Expression
import expressions.numerical.NumFraction


data class Monomial(override val body: Pair<NumFraction, Map<Char, Int>>) : Expression() {
    val coeff
        get() = body.first
    val varMap
        get() = body.second

    override fun simplified(): Expression { return this }
    override fun simplifiedSoftly(): Monomial { return this }

    operator fun unaryMinus(): Monomial {
        return Monomial(-coeff to varMap)
    }

    operator fun times(other: Monomial): Monomial {
        val newCoeff = this.coeff * other.coeff
        val newVarMap = varMap.toMutableMap()
        for ((k, v) in other.varMap) {
            newVarMap[k] = newVarMap.getOrDefault(k, 0) + v
        }
        return Monomial(newCoeff to newVarMap)
    }
    operator fun times(other: Int): Monomial {
        return Monomial(coeff * other to varMap)
    }

    operator fun div(other: Int): Monomial {
        return Monomial(coeff / other to varMap)
    }
}