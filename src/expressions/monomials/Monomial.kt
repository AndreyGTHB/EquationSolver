package expressions.monomials

import expressions.Expression
import expressions.ReducibleExpression
import expressions.binary.Quotient
import expressions.longs.Product
import expressions.longs.Sum
import expressions.numerical.Fraction
import utils.toFraction
import kotlin.math.min


class Monomial private constructor(
    override val body: Pair<Fraction, Map<Char, Int>>,
    final: Boolean
) : ReducibleExpression(final) {
    val coeff = body.first
    val varMap = body.second

    constructor(body: Pair<Fraction, Map<Char, Int>>) : this(body, false)

    override fun simplify(): Expression {
        if (final) { return this }

        val simpleMonomial = simplifySoftly()
        if (simpleMonomial.coeff.isNull()) return Fraction(0 to 1)
        return simpleMonomial
    }
    override fun simplifySoftly(): Monomial {
        if (final) { return this }

        val simpleCoeff = coeff.simplify()
        val sortedVarMap = varMap.toSortedMap()
        val sortedBody = simpleCoeff to sortedVarMap
        return Monomial(sortedBody, true)
    }

    override fun commonFactor(other: Expression): Expression {
        if (!final) throw RuntimeException("A non-simplified monomial cannot be reduced")
        return when (val simpleOther = other.simplify()) {
            is Monomial -> commonFactorWithMonomial(simpleOther)
            is Product -> commonFactorWithProduct(simpleOther)
            is Sum -> commonFactorWithSum(simpleOther)
            is Quotient -> commonFactor(simpleOther.numer)
            else -> 1.toFraction()
        }

    }
    private fun commonFactorWithMonomial(other: Monomial): Expression {
        val commonVarMap = mutableMapOf<Char, Int>()
        varMap.forEach{ (v, thisDegree) ->
            val otherDegree = other.varMap[v] ?: 0
            val newDegree = min(thisDegree, otherDegree)
            if (newDegree != 0) commonVarMap[v] = newDegree
        }
        if (commonVarMap.isEmpty()) return 1.toFraction()
        return Monomial(1.toFraction() to commonVarMap)
    }
    private fun commonFactorWithProduct(other: Product): Expression {
        val finalVarMap = mutableMapOf<Char, Int>()
        other.body.forEach {
            val currCF = commonFactor(it)
            if (currCF is Monomial) {
                currCF.varMap.forEach {(v, d) -> finalVarMap[v] = (finalVarMap[v] ?: 0) + d }
            }
        }
        val finalMonomial = Monomial(1.toFraction() to finalVarMap)
        return commonFactorWithMonomial(finalMonomial)
    }
    private fun commonFactorWithSum(other: Sum): Expression {
        val commonSumFactor = other.factorOut()
        return commonFactor(commonSumFactor)
    }

    override fun reduceOrNull(other: Expression): Expression? {
        if (this.final) throw RuntimeException("A non-simplified monomial cannot be reduced")
        return when (val simpleOther = other.simplify()) {
            is Fraction -> 1.toFraction()
            is Monomial -> reduceByMonomialOrNull(simpleOther)
            else -> null
        }
    }
    private fun reduceByMonomialOrNull(other: Monomial): Monomial? {
        val newCoeff = this.coeff / other.coeff
        val newVarMap = this.varMap.toMutableMap()
        for ((v, d) in other.varMap) {
            newVarMap[v] = (this.varMap[v] ?: 0) - d
            if (newVarMap[v]!! < 0) return null
            if (newVarMap[v]!! == 0) newVarMap.remove(v)
        }
        return Monomial(newCoeff to newVarMap)
    }

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