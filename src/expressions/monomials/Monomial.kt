package expressions.monomials

import expressions.Expression
import expressions.numerical.NumFraction


data class Monomial(override val body: Pair<NumFraction, Map<Char, Int>>) : Expression() {
    val coeff = body.first
    val varMap = body.second

    override fun simplified(): Expression { return this }
    override fun simplifiedSoftly(): Monomial { return this }

    override operator fun unaryMinus(): Monomial {
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
    operator fun times(other: NumFraction): Monomial {
        return Monomial(coeff * other to varMap)
    }

    operator fun div(other: Int): Monomial {
        return Monomial(coeff / other to varMap)
    }
    operator fun div(other: NumFraction): Monomial {
        return Monomial(coeff / other to varMap)
    }
    fun divByMonomialOrNull(other: Monomial): Monomial? {
        val newCoeff = this.coeff / other.coeff
        val newVarMap = mutableMapOf<Char, Int>()
        for ((k, v) in this.varMap) {
            val newPower = v - (other.varMap[k] ?: 0)
            if (newPower < 0) return null
            if (newPower != 0) newVarMap[k] = newPower
        }
        return Monomial(newCoeff to newVarMap)
    }
}