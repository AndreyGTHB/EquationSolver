package expressions.longs

import expressions.Expression
import expressions.binary.Quotient
import expressions.monomials.Monomial
import expressions.number.Rational
import expressions.commonFactor
import expressions.zero
import utils.toVarMap
import utils.varMapToString

class Sum private constructor(body: List<Expression>, final: Boolean) : LongExpression(body, final) {
    constructor(body: List<Expression>) : this(body, false)

    override fun simplify(): Expression {
        if (final) return this

        val simpleSum = simplifySoftly()
        return when (simpleSum.body.size) {
            0 -> zero()
            1 -> simpleSum.body.first()
            else -> Sum(simpleSum.body, true)
        }
    }
    private fun simplifySoftly(): Sum {
        if (final) return this

        var prevBody = simplifyBody()
        var currBody = mutableListOf<Expression>()
        // Associativity
        prevBody.forEach { term ->
            if (term is Sum) { term.body.forEach { subTerm -> currBody.add(subTerm) } }
            else                                              currBody.add(term)
        }

        // Reduction of terms with same non-rational part
        prevBody = currBody
        currBody = mutableListOf()
        var freeTerm = zero()
        val varMapStrings = mutableMapOf<String, Rational>()
        val quotientMap = mutableMapOf<Expression, Expression>()
        prevBody.forEach { term ->
            when (term) {
                is Rational -> { freeTerm += term }
                is Monomial -> {
                    val varMapString = varMapToString(term.varMap)
                    varMapStrings[varMapString] = (varMapStrings[varMapString] ?: zero()) + term.coeff
                }
                is Quotient -> {
                    quotientMap[term.denom] = (quotientMap[term.denom] ?: zero()) + term.numer
                }
                else ->        { currBody.add(term) }
            }
        }
        freeTerm = freeTerm.simplify()
        if (!freeTerm.isZero()) currBody.add(freeTerm)
        varMapStrings.forEach { vms, coeff ->
            val vm = vms.toVarMap()
            if (!coeff.isZero()) { currBody.add(Monomial(coeff to vm).simplify()) }
        }
        quotientMap.forEach { denom, numer ->
            val simpleQt = Quotient(numer to denom).simplify()
            if (!simpleQt.isZeroRational()) currBody.add(simpleQt)
        }

        currBody.sort()
        return Sum(currBody)
    }

    override fun commonFactor(other: Expression): Expression {
        return commonFactor(commonInternalFactor(), other)
    }
    fun commonInternalFactor(): Expression {
        var cf: Expression = zero()
        body.forEach {
            cf = commonFactor(cf, it)
        }
        return cf
    }

    override fun _reduceOrNull(other: Expression): Expression? {
        val newBody = body.map { it.reduceOrNull(other) ?: return null }
        return Sum(newBody)
    }

    override operator fun unaryMinus(): Sum {
        val newBody = body.map { -it }.toList()
        return Sum(newBody)
    }

    override operator fun plus(other: Expression):  Sum {
        return Sum(body + listOf(other))
    }
    override operator fun minus(other: Expression): Sum {
        return Sum(body + listOf(-other))
    }
}