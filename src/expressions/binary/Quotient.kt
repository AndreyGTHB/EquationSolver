package expressions.binary

import expressions.Expression
import expressions.longs.LongExpression
import expressions.longs.Sum
import expressions.monomials.Monomial
import expressions.numerical.Fraction

class Quotient private constructor(body: Pair<Expression, Expression>, final: Boolean) : BinaryExpression(body, final) {
    val numer = body.first
    val denom = body.second

    constructor(body: Pair<Expression, Expression>) : this(body, false)

    override fun simplify(): Expression {
        if (final) return this

        val simplifiedQuotient = simplifySoftly()
        if (simplifiedQuotient.denom !is LongExpression) return simplifiedQuotient.singleDenomSimp()
        return simplifiedQuotient
    }

    override fun simplifySoftly(): Quotient {
        return Quotient(simplifiedBody())
    }


    private fun singleDenomSimp(): Expression {
        return when (denom) {
            is Fraction ->  numer / denom
            is Monomial -> monomialDenomSimp()
            else -> throw RuntimeException("The denominator is not single")
        }
    }
    private fun monomialDenomSimp(): Expression {
        denom as Monomial
        return when (numer) {
            is Monomial -> numer.divByMonomialOrNull(denom) ?: this
            is Sum -> sumNumerMonomialDenomSimp()
            else -> return this
        }
    }
    private fun sumNumerMonomialDenomSimp(): Expression {
        numer as Sum
        denom as Monomial
        val newBody = mutableListOf<Expression>()
        for (term in numer.body) {
            if (term !is Monomial) return this
            newBody.add(term.divByMonomialOrNull(denom) ?: return this)
        }
        return Sum(newBody)
    }


    override operator fun unaryMinus(): Quotient {
        return Quotient(-numer to denom)
    }
}