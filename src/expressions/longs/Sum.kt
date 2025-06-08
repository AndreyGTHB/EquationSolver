package expressions.longs

import expressions.Expression
import expressions.binary.Quotient
import expressions.number.Rational
import expressions.commonFactor
import expressions.unit
import expressions.zero

class Sum private constructor(body: List<Expression>, final: Boolean) : LongExpression(body, final) {
    constructor(body: List<Expression>) : this(body, false)

    override fun simplify(): Expression {
        if (final) return this

        val simpleSum = simplifySoftly()
        return when (simpleSum.body.size) {
            0 ->    zero()
            1 ->    simpleSum.body.first()
            else -> simpleSum
        }
    }
    private fun simplifySoftly(): Sum {
        var prevBody = simplifyBody()
        var currBody = mutableListOf<Expression>()
        // Associativity
        prevBody.forEach { term ->
            if (term is Sum) { term.body.forEach { subTerm -> currBody.add(subTerm) } }
            else                                              currBody.add(term)
        }

        // Bringing quotients to the common denominator
        prevBody = currBody
        currBody = mutableListOf()
        val quotientMap = mutableMapOf<Expression, Expression>()
        prevBody.forEach { term ->
            if (term is Quotient) quotientMap[term.denom] = (quotientMap[term.denom] ?: zero()) + term.numer
            else                  currBody.add(term)
        }
        quotientMap.forEach { denom, numer ->
            val reducedQuotient = (numer / denom).simplify()
            if (!reducedQuotient.isZeroRational()) currBody.add(reducedQuotient)
        }

        // Reduction of terms with the same non-rational part
        val termMap1 = mutableMapOf<Expression, Rational>()
        currBody.forEach { term ->
            val nonRationalPart = term.nonRationalPart()
            val rationalPart = term.rationalPart()
            termMap1[nonRationalPart] = (termMap1[nonRationalPart] ?: zero()) + rationalPart
        }
        currBody.clear()
        termMap1.forEach { expr, coeff ->
            val reducedTerm = (coeff * expr).simplify()
            if (!reducedTerm.isZeroRational()) currBody.add(reducedTerm)
        }

        // Reduction of terms with the same non-numerical part
        prevBody = currBody
        currBody = mutableListOf()
        val termMap2 = mutableMapOf<Expression, Expression>()
        prevBody.forEach { term ->
            val nonNumPart = term.nonNumericalPart()
            val numPart = term.numericalPart()
            if (nonNumPart.isUnitRational()) currBody.add(term)
            else {
                val prevCoeff = termMap2[nonNumPart]
                termMap2[nonNumPart] = if (prevCoeff == null) numPart else prevCoeff + numPart
            }
        }
        termMap2.forEach { expr, coeff ->
            val reducedTerm = (coeff * expr).simplify(false)
            if (!reducedTerm.isZeroRational()) currBody.add(reducedTerm)
        }

        currBody.sort()
        return Sum(currBody, true)
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