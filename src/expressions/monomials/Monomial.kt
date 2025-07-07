package expressions.monomials

import expressions.Expression
import expressions.binary.Power
import expressions.zero
import expressions.number.Rational
import expressions.number.min
import expressions.unit


class Monomial internal constructor (
    override val body: Map<Char, Rational>,
    final: Boolean
) : Expression(final=final) {
    constructor(body: Map<Char, Rational>) : this(body, false)
    constructor(vararg body: Pair<Char, Rational>) : this(body.toMap())

    val varMap = body

    override val isNumber = varMap.isEmpty()

    override fun _simplify(): Expression {
        val sMonomial = simplifySoftly()
        if (sMonomial.varMap.isEmpty()) return unit()

        val numerVarMap = varMap.toMutableMap()
        val denomVarMap = mutableMapOf<Char, Rational>()
        sMonomial.varMap.forEach { v, exp ->
            if (exp.isNegative()) {
                denomVarMap[v] = -exp
                numerVarMap.remove(v)
            }
        }
        return  if (numerVarMap.isNotEmpty() && denomVarMap.isNotEmpty()) {
                    Monomial(numerVarMap, true) / Monomial(denomVarMap, true)
                } else if (denomVarMap.isNotEmpty()) unit() / Monomial(denomVarMap, true)
                  else                               sMonomial
    }
    private fun simplifySoftly(): Monomial {
        val sVarMap = varMap
            .filterValues { !it.isZero() }
            .mapValues { it.value.simplify() }
            .toSortedMap()
        return Monomial(sVarMap)
    }

    override fun firstVariable() = if (body.isNotEmpty()) body.keys.first() else null
    override fun contains(variable: Char) = body.contains(variable)
    override fun _substitute(variable: Char, value: Expression): Expression {
        val newMonomialBody = body.toMutableMap()
        val substituted = body[variable]?.let { exp ->
            newMonomialBody.remove(variable)
            value raisedTo exp
        }
        return if (substituted == null) this
          else if (newMonomialBody.isEmpty()) substituted
          else                                Monomial(newMonomialBody) * substituted
    }

    override fun _commonFactor(other: Expression): Expression? {
        return when (other) {
            is Monomial -> commonFactorWithMonomial(other)
            else -> null
        }

    }
    private fun commonFactorWithMonomial(other: Monomial): Monomial? {
        val commonVarMap = mutableMapOf<Char, Rational>()
        varMap.forEach { (v, thisDegree) ->
            val otherDegree = other.varMap[v] ?: zero()
            val newDegree = min(thisDegree, otherDegree)
            if (!newDegree.isZero()) commonVarMap[v] = newDegree
        }
        if (commonVarMap.isEmpty()) return null
        return Monomial(commonVarMap)
    }

    override fun _reduceOrNull(other: Expression): Expression? {
        return when (other) {
            is Monomial -> reduceByMonomialOrNull(other)
            else -> null
        }
    }
    private fun reduceByMonomialOrNull(other: Monomial): Expression? {
        val newVarMap = this.varMap.toMutableMap()
        for ((v, d) in other.varMap) {
            newVarMap[v] = (this.varMap[v] ?: zero()) - d
            if (newVarMap[v]!!.isNegative()) return null
            if (newVarMap[v]!!.isZero()) newVarMap.remove(v)
        }
        return Monomial(newVarMap)
    }

    operator fun times(other: Monomial): Monomial {
        val newVarMap = varMap.toMutableMap()
        for ((v, d) in other.varMap) {
            newVarMap[v] = (newVarMap[v] ?: zero()) + d
        }
        return Monomial(newVarMap)
    }
    fun power(exp: Rational): Expression {
        val newVarMap = varMap.mapValues { (_, d) -> (d * exp).simplify() }
        return if (final && exp.isPositive()) Monomial(newVarMap, true)
          else                                Monomial(newVarMap)
    }

    override fun toString(): String {
        var str = "M: "
        for ((v, d) in varMap) {
            str += "$v^$d "
        }
        str = str.slice(0 until str.lastIndex)
        return str
    }
}
