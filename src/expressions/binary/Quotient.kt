package expressions.binary

import expressions.Expression
import expressions.longs.LongExpression
import expressions.longs.Sum
import expressions.monomials.Monomial
import expressions.numerical.NumFraction

class Quotient(body: Pair<Expression, Expression>) : BinaryExpression(body) {
    val numer = body.first
    val denom = body.second

    override fun simplified(): Expression {
        val simplifiedQuotient = simplifiedSoftly()
        if (simplifiedQuotient.denom !is LongExpression) return simplifiedQuotient.singleDenomSimp()
        return simplifiedQuotient
    }

    override fun simplifiedSoftly(): Quotient {
        val simplifiedBody = simplifiedBody()
        return Quotient(simplifiedBody)
    }


    private fun singleDenomSimp(): Expression {
        return when (denom) {
            is NumFraction ->  numer / denom
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


    override operator fun unaryMinus(): Sum {
        TODO("to implement")
    }
}