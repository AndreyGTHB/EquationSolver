package expressions.longs

import expressions.Expression
import expressions.monomials.Monomial
import expressions.numerical.Fraction
import expressions.commonFactor
import expressions.nullFraction
import utils.toFraction
import utils.toVarMap
import utils.varMapToString

class Sum private constructor(body: List<Expression>, final: Boolean) : LongExpression(body, final) {
    constructor(body: List<Expression>) : this(body, false)

    override fun simplify(): Expression {
        if (final) return this

        val simpleSum = simplifySoftly()
        return when (simpleSum.body.size) {
            0 -> Fraction(0 to 1)
            1 -> simpleSum.body.first()
            else -> Sum(simpleSum.body, true)
        }
    }
    override fun simplifySoftly(): Sum {
        if (final) return this

        var prevBody = simplifyBody()
        var currBody = mutableListOf<Expression>()
        // Associativity
        prevBody.forEach { term ->
            if (term is Sum) { term.body.forEach { subTerm -> currBody.add(subTerm) } }
            else                                              currBody.add(term)
        }

        // Reduction of similar terms
        prevBody = currBody
        currBody = mutableListOf()
        var freeTerm = Fraction(0 to 1)
        val varMapStrings: MutableMap<String, Fraction> = mutableMapOf()
        prevBody.forEach { term ->
            when (term) {
                is Fraction -> { freeTerm += term }
                is Monomial -> {
                    val varMapString = varMapToString(term.varMap)
                    varMapStrings[varMapString] = (varMapStrings[varMapString] ?: nullFraction()) + term.coeff
                }
                else ->        { currBody.add(term) }
            }
        }

        freeTerm = freeTerm.simplify()
        if (!freeTerm.isNull()) currBody.add(freeTerm)
        varMapStrings.forEach { (vms, coeff) ->
            val vm = vms.toVarMap()
            if (!coeff.isNull()) { currBody.add(Monomial(coeff to vm).simplify()) }
        }
        return Sum(currBody)
    }

    override fun commonFactor(other: Expression): Expression {
        return commonFactor(commonInternalFactor(), other)
    }
    fun commonInternalFactor(): Expression {
        var cf: Expression = 0.toFraction()
        body.forEach {
            cf = commonFactor(cf, it)
        }
        return cf
    }

    override fun reduceOrNull(other: Expression): Expression? {
        val newBody = body.map { it.reduceOrNull(other) ?: return null }
        return Sum(newBody).simplify()
    }

    override operator fun unaryMinus(): Sum {
        val newBody = body.map { -it }.toList()
        return Sum(newBody)
    }

    override operator fun plus(other: Expression):  Sum {
        return Sum(body + listOf(other))
    }
}