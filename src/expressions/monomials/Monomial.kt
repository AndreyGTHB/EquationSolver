package expressions.monomials

import expressions.Expression
import expressions.binary.Quotient
import expressions.longs.Product
import expressions.longs.Sum
import expressions.nullFraction
import expressions.numerical.Fraction
import utils.toFraction
import kotlin.math.min


class Monomial private constructor(override val body: Pair<Fraction, Map<Char, Int>>, final: Boolean) : Expression(final) {
    constructor(body: Pair<Fraction, Map<Char, Int>>) : this(body, false)

    val coeff = body.first
    val varMap = body.second

    override fun simplify(): Expression {
        if (final) { return this }

        val simpleMonomial = simplifySoftly()
        if (simpleMonomial.coeff.isNull()) return nullFraction()
        return simpleMonomial
    }
    override fun simplifySoftly(): Monomial {
        if (final) { return this }

        val simpleCoeff = coeff.simplify()
//        val sortedVarMap = varMap.toSortedMap()
//        val sortedBody = simpleCoeff to sortedVarMap
        return Monomial(simpleCoeff to varMap, true)
    }

    override fun commonFactor(other: Expression): Expression? {
        return when (other) {
            is Monomial -> commonFactorWithMonomial(other)
            else -> null
        }

    }
    private fun commonFactorWithMonomial(other: Monomial): Monomial? {
        val commonVarMap = mutableMapOf<Char, Int>()
        varMap.forEach{ (v, thisDegree) ->
            val otherDegree = other.varMap[v] ?: 0
            val newDegree = min(thisDegree, otherDegree)
            if (newDegree != 0) commonVarMap[v] = newDegree
        }
        if (commonVarMap.isEmpty()) return null
        return Monomial(1.toFraction() to commonVarMap)
    }

    override fun reduceOrNull(other: Expression): Expression? {
        if (!(this.final && other.final)) throw RuntimeException("A non-simplified monomial cannot be reduced")
        return when (other) {
            is Fraction -> Monomial(coeff / other to varMap).simplify()
            is Monomial -> reduceByMonomialOrNull(other)
            else -> null
        }
    }
    private fun reduceByMonomialOrNull(other: Monomial): Expression? {
        val newCoeff = this.coeff / other.coeff
        val newVarMap = this.varMap.toMutableMap()
        for ((v, d) in other.varMap) {
            newVarMap[v] = (this.varMap[v] ?: 0) - d
            if (newVarMap[v]!! < 0) return null
            if (newVarMap[v]!! == 0) newVarMap.remove(v)
        }
        return Monomial(newCoeff to newVarMap).simplify()
    }

    fun isUnit(): Boolean = simplify().equals(1)

    override operator fun unaryMinus(): Monomial {
        return Monomial(-coeff to varMap)
    }

    operator fun times(other: Monomial): Monomial {
        val newCoeff = this.coeff * other.coeff
        val newVarMap = varMap.toMutableMap()
        for ((v, d) in other.varMap) {
            newVarMap[v] = (newVarMap[v] ?: 0) + d
        }
        return Monomial(newCoeff to newVarMap)
    }
    operator fun times(other: Int): Monomial {
        return Monomial(coeff * other to varMap)
    }
    operator fun times(other: Fraction): Monomial {
        return Monomial(coeff * other to varMap)
    }

    operator fun div(other: Int): Monomial {
        return Monomial(coeff / other to varMap)
    }
    operator fun div(other: Fraction): Monomial {
        return Monomial(coeff / other to varMap)
    }


    override fun toString(): String {
        var str = "M:  $coeff "
        for ((v, d) in varMap) {
            str += "$v^$d "
        }
        return str
    }
}
