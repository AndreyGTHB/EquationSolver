package expressions.binary

import expressions.Expression
import expressions.longs.Sum
import expressions.monomials.Monomial
import expressions.numerical.NumFraction

class Quotient(body: Pair<Expression, Expression>) : BinaryExpression(body) {
    val numer = body.first
    val denom = body.second

    override fun simplified(): Expression {
        simplifiedSoftly()
        return this
    }

    override fun simplifiedSoftly(): Quotient { return this }


    private fun singleDenomBodySimp(): Pair<Expression, Expression> {
        var newBody = emptyBody()
        when (denom) {
            is NumFraction -> newBody = numer / denom to NumFraction(1 to 1)
            is Monomial -> {}
        }
    }
    private fun monomialDenomBodySimp(): Pair<Expression, Expression> {
        denom as Monomial
        when (numer) {
            is Monomial -> return numer /
        }
    }


    override operator fun unaryMinus(): Sum {
        TODO("to implement")
    }
}