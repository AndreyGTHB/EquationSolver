package expressions.monomials

import expressions.Expression
import expressions.zero
import expressions.numerical.Rational
import expressions.unit
import utils.min


class Monomial private constructor(override val body: Pair<Rational, Map<Char, Rational>>, final: Boolean) : Expression(final) {
    constructor(body: Pair<Rational, Map<Char, Rational>>) : this(body, false)

    val coeff = body.first
    val varMap = body.second

    override fun simplify(): Expression {
        if (final) { return this }

        val simpleMonomial = simplifySoftly()
        if (simpleMonomial.coeff.isNull()) return zero()
        if (simpleMonomial.varMap.isEmpty()) return simpleMonomial.coeff
        return simpleMonomial
    }
    override fun simplifySoftly(): Monomial {
        if (final) { return this }

        val simpleCoeff = coeff.simplify()
        val simpleVarMap = varMap
            .filterValues { !it.isNull() }
            .mapValues { it.value.simplify() }
            .toSortedMap()
//        val sortedBody = simpleCoeff to sortedVarMap
        return Monomial(simpleCoeff to simpleVarMap, true)
    }

    override fun commonFactor(other: Expression): Expression? {
        return when (other) {
            is Monomial -> commonFactorWithMonomial(other)
            else -> null
        }

    }
    private fun commonFactorWithMonomial(other: Monomial): Monomial? {
        val commonVarMap = mutableMapOf<Char, Rational>()
        varMap.forEach{ (v, thisDegree) ->
            val otherDegree = other.varMap[v] ?: zero()
            val newDegree = min(thisDegree, otherDegree)
            if (!newDegree.isNull()) commonVarMap[v] = newDegree
        }
        if (commonVarMap.isEmpty()) return null
        return Monomial(unit() to commonVarMap)
    }

    override fun reduceOrNull(other: Expression): Expression? {
        if (!(this.final && other.final)) throw RuntimeException("A non-simplified monomial cannot be reduced")
        return when (other) {
            is Rational -> Monomial(coeff / other to varMap).simplify()
            is Monomial -> reduceByMonomialOrNull(other)
            else -> null
        }
    }
    private fun reduceByMonomialOrNull(other: Monomial): Expression? {
        val newCoeff = this.coeff / other.coeff
        val newVarMap = this.varMap.toMutableMap()
        for ((v, d) in other.varMap) {
            newVarMap[v] = (this.varMap[v] ?: zero()) - d
            if (newVarMap[v]!!.isNegative()) return null
            if (newVarMap[v]!!.isNull()) newVarMap.remove(v)
        }
        return Monomial(newCoeff to newVarMap).simplify()
    }

    fun isUnit(): Boolean = simplify().equals(1)

    override operator fun unaryMinus(): Monomial {
        return Monomial(-coeff to varMap, final)
    }

    operator fun times(other: Monomial): Monomial {
        val newCoeff = this.coeff * other.coeff
        val newVarMap = varMap.toMutableMap()
        for ((v, d) in other.varMap) {
            newVarMap[v] = (newVarMap[v] ?: zero()) + d
        }
        return Monomial(newCoeff to newVarMap)
    }
    operator fun times(other: Int): Monomial {
        return Monomial(coeff * other to varMap)
    }
    operator fun times(other: Rational): Monomial {
        return Monomial(coeff * other to varMap)
    }

    operator fun div(other: Int): Monomial {
        return Monomial(coeff / other to varMap)
    }
    operator fun div(other: Rational): Monomial {
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
