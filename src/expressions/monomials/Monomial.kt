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

        val simplifiedMonomial = simplifySoftly()
        if (simplifiedMonomial.coeff.isNull()) return Fraction(0 to 1)
        return simplifiedMonomial
    }
    override fun simplifySoftly(): Monomial {
        if (final) { return this }

        val simplifiedCoeff = coeff.simplify()
        val sortedVarMap = varMap.toSortedMap()
        val sortedBody = simplifiedCoeff to sortedVarMap
        return Monomial(sortedBody, true)
    }

    override fun commonFactor(o: ReducibleExpression): Expression {
        if(!final) throw RuntimeException ("Cannot be called on a non-simplified expression")
        val other = o.simplify()
        if (other is Fraction) {
            return if (other.isNull()) this else 1.toFraction()
        }

        val commonFactor = when (other) {
            is Monomial -> commonFactorWithMonomial(other)
            is Product -> commonFactorWithProduct(other)
            is Sum -> commonFactorWithSum(other)
            is Quotient -> commonFactor(other.numer)
            else -> 1.toFraction()
        }
        return commonFactor

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
        val totalVarMap = mutableMapOf<Char, Int>()
        other.body.forEach {
            val currCF = commonFactor(it)
            if (currCF is Monomial) {
                currCF.varMap.forEach {(v, d) -> totalVarMap[v] = (totalVarMap[v] ?: 0) + d }
            }
        }
        val totalMonomial = Monomial(1.toFraction() to totalVarMap)
        return commonFactorWithMonomial(totalMonomial)
    }
    private fun commonFactorWithSum(other: Sum): Expression {
        val commonSumFactor = other.factorOut()
        return commonFactor(commonSumFactor)
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
    fun divByMonomialOrNull(other: Monomial): Monomial? {
        val newCoeff = this.coeff / other.coeff
        val newVarMap = this.varMap.toMutableMap()
        for ((v, d) in other.varMap) {
            newVarMap[v] = (this.varMap[v] ?: 0) - d
            if (newVarMap[v]!! < 0) return null
            if (newVarMap[v]!! == 0) newVarMap.remove(v)
        }
        return Monomial(newCoeff to newVarMap)
    }


    override fun toString(): String {
        var str = "M:  $coeff "
        for ((v, d) in varMap) {
            str += "$v^$d "
        }
        return str
    }
}