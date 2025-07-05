package expressions.longs

import expressions.*
import expressions.binary.Quotient
import expressions.number.Rational

class Sum (
    body: List<Expression>,
) : LongExpression(body, final=false) {
    constructor(vararg body: Expression) : this(body.toList())

    val commonInternalFactor by lazy(::genCommonInternalFactor)

    override fun _simplify(): Expression {
        val sThis = simplifySoftly()
        return when (sThis.body.size) {
            0    -> zero()
            1    -> sThis.body[0]
            else -> sThis
        }
    }
    private fun simplifySoftly(): Sum {
        val newBody = simplifyBody()
            .expandSums()
            .combineByNonRationalPart()
            .combineByNonNumericalPart()
            .combineQuotients()
            .sorted()
        return Sum(newBody)
    }

    private fun List<Expression>.expandSums(): List<Expression> {
        val newBody = emptyBody()
        forEach { term ->
            when (term) {
                is Sum -> term.body.forEach { subTerm -> newBody.add(subTerm) }
                is Product -> {
                    val expanded = term.expandBrackets()
                    expanded.body.forEach { subTerm -> newBody.add(subTerm.simplify()) }
                }
                is Quotient -> { when (term.numer) {
                    is Sum -> term.numer.body.forEach { numerSubTerm -> newBody.add((numerSubTerm / term.denom).simplify()) }
                    is Product -> {
                        val expandedNumer = term.numer.expandBrackets()
                        expandedNumer.body.forEach { numerSubTerm -> newBody.add((numerSubTerm / term.denom).simplify()) }
                    }
                    else -> newBody.add(term)
                }}
                else -> newBody.add(term)
            }
        }
        return newBody
    }

    private fun List<Expression>.combineByNonRationalPart(): List<Expression> {
        val newBody = emptyBody()
        val termMap = mutableMapOf<Expression, Rational>()
        forEach { term ->
            val nonRationalPart = term.nonRationalPart()
            val rationalPart = term.rationalPart()
            termMap[nonRationalPart] = (termMap[nonRationalPart] ?: zero()) + rationalPart
        }
        termMap.forEach { expr, coeff ->
            val reducedTerm = (coeff * expr).simplify()
            if (!reducedTerm.isZeroRational()) newBody.add(reducedTerm)
        }
        return newBody
    }

    private fun List<Expression>.combineByNonNumericalPart(): List<Expression> {
        val newBody = emptyBody()
        val termMap = mutableMapOf<Expression, Expression>()
        forEach { term ->
            val nonNumPart = term.nonNumericalPart()
            val numPart = term.numericalPart()
            if (nonNumPart.isUnitRational()) newBody.add(term)
            else                             termMap[nonNumPart] = termMap[nonNumPart]?.plus(numPart) ?: numPart
        }
        termMap.forEach { expr, coeff ->
            val reducedTerm = (coeff * expr).simplify()
            if (!reducedTerm.isZeroRational()) newBody.add(reducedTerm)
        }
        return newBody
    }

    private fun List<Expression>.combineQuotients(): List<Expression> {
        val newBody = emptyBody()
        val quotientMap = mutableMapOf<Expression, Expression>()
        forEach { term ->
            if (term is Quotient) quotientMap[term.denom] = (quotientMap[term.denom] ?: zero()) + term.numer
            else                  newBody.add(term)
        }
        quotientMap.forEach { denom, numer ->
            val reducedQuotient = (numer / denom).simplify()
            if (!reducedQuotient.isZeroRational()) newBody.add(reducedQuotient)
        }
        return newBody
    }

    override fun _commonFactor(other: Expression): Expression {
        return commonFactor(commonInternalFactor, other)
    }
    private fun genCommonInternalFactor(): Expression {
        assert(final)

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
        return Sum(body + other)
    }
    override operator fun minus(other: Expression): Sum {
        return Sum(body + (-other))
    }
}