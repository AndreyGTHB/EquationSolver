package expressions.longs

import expressions.*
import expressions.binary.Quotient
import expressions.monomials.Variable
import expressions.number.Rational

class Sum (
    body: List<Expression> = listOf(),
    final: Boolean = false
) : LongExpression(body, final=final) {
    constructor(vararg body: Expression) : this(body.toList())

    val commonInternalFactor by lazy(::genCommonInternalFactor)
    val separatedWithCommonInternalFactor by lazy(::genSeparatedWithCommonInternalFactor)

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

    override fun _substitute(variable: Char, value: Expression) = Sum(substituteIntoBody(variable, value))

    override fun _commonFactor(other: Expression) = when (other) {
        is Sum     -> commonFactorWithSum(other)
        is Product -> null
        else       -> commonFactor(commonInternalFactor, other)
    }
    private fun commonFactorWithSum(other: Sum): Expression {
        val reducedThis = this.reduce(this.commonInternalFactor) as Sum
        val reducedOther = other.reduce(other.commonInternalFactor) as Sum

        var cf = commonFactor(this.commonInternalFactor, other.commonInternalFactor)
        if (reducedThis == reducedOther || reducedThis == reducedOther.opposite()) cf *= reducedThis
        return cf
    }

    private fun genCommonInternalFactor(): Expression {
        assert(final)

        var cf: Expression = zero()
        body.forEach {
            cf = commonFactor(cf, it)
        }
        return cf
    }
    private fun genSeparatedWithCommonInternalFactor(): Pair<Expression, Sum> {
        return commonInternalFactor to (reduceEachTermOrNull(commonInternalFactor)!! as Sum)
    }

    fun opposite(): Sum {
        val newBody = if (final) body.map { (-it).simplify() }.sorted()
                      else       body.map { -it }
        return Sum(newBody, final)
    }

    override fun _reduceOrNull(other: Expression): Expression? {
        return when (other) {
            is Sum     -> reduceBySumOrNull(other)
            is Product -> reduceByProductOrNull(other)
            else       -> reduceEachTermOrNull(other)
        }
    }

    private fun reduceBySumOrNull(other: Sum): Expression? {
        val thisProductBody = this.separatedWithCommonInternalFactor.run { mutableListOf(first, second) }
        val otherProductBody = other.separatedWithCommonInternalFactor.run { mutableListOf(first, second) }
        thisProductBody.replaceAll { thisFactor ->
            var currThisFactor = thisFactor
            otherProductBody.replaceAll { otherFactor ->
                var currOtherFactor = otherFactor
                if (currThisFactor is Sum) {
                    if (otherFactor is Sum) {
                        if (currThisFactor == otherFactor) {
                            currThisFactor = unit()
                            currOtherFactor = unit()
                        }
                        else if (currThisFactor == otherFactor.opposite()) {
                            currThisFactor = -unit()
                            currOtherFactor = unit()
                        }
                    }
                    else {
                        val cf = commonFactor(currThisFactor, otherFactor)
                        currThisFactor = currThisFactor.reduce(cf)
                        currOtherFactor = otherFactor.reduce(cf)
                    }
                }
                else {
                    val cf = commonFactor(currThisFactor, otherFactor)
                    currThisFactor = currThisFactor.reduce(cf)
                    currOtherFactor = otherFactor.reduce(cf)
                }
                currOtherFactor
            }
            currThisFactor
        }

        otherProductBody.forEach { if(!it.isUnitRational()) return null }
        return Product(thisProductBody)
    }

    private fun reduceByProductOrNull(other: Product): Expression? {
        val separated = Product(separatedWithCommonInternalFactor.toList(), true)
        return separated.reduceOrNull(other)
    }

    private fun reduceEachTermOrNull(other: Expression): Expression? {
        assert(this.final && other.final)
        val newBody = body.map { it.reduceOrNull(other) ?: return null }.sorted()
        return Sum(newBody, true)
    }

    override fun unaryMinus(): Sum {
        val newBody = body.map { -it }.toList()
        return Sum(newBody)
    }

    override fun _plus(other: Expression):  Sum {
        return Sum(body + other)
    }
    override fun _minus(other: Expression): Sum {
        return Sum(body + (-other))
    }
}