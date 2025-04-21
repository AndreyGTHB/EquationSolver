package expressions.binary

import expressions.Expression
import expressions.number.Rational
import expressions.commonFactor

class Quotient private constructor(body: Pair<Expression, Expression>, final: Boolean) : BinaryExpression(body, final) {
    constructor(body: Pair<Expression, Expression>) : this(body, false)

    val numer = body.first
    val denom = body.second

    override fun simplify(): Expression {
        if (final) return this

        val simpleThis = simplifySoftly()
        if (simpleThis.denom is Rational) return simpleThis.numer.reduce(simpleThis.denom)
        return Quotient(simpleThis.body, true)
    }
    override fun simplifySoftly(): Quotient {
        if (final) return this

        val (simpleNumer, simpleDenom) = simplifyBody()
        val cf = commonFactor(simpleNumer, simpleDenom)
        val reducedNumer = simpleNumer.reduce(cf)
        val reducedDenom = simpleDenom.reduce(cf)
        return Quotient(reducedNumer to reducedDenom)
    }

    override fun commonFactor(other: Expression): Expression {
        return commonFactor(numer, other)
    }

    override fun reduceOrNull(other: Expression): Expression? {
        if (!this.final && other.final) throw RuntimeException("A non-simplified quotient cannot be reduced")
        val reducedNumer = numer.reduceOrNull(other) ?: return null
        val reducedThis = Quotient(reducedNumer to denom)
        return reducedThis.simplify()
    }

    fun sumAsQuotient(other: Quotient): Quotient {
        val newNumer = this.numer * other.denom + other.numer * this.denom
        val newDenom = this.denom * other.denom
        return Quotient(newNumer to newDenom)
    }

    operator fun times(other: Quotient): Quotient {
        val newNumer = this.numer * other.numer
        val newDenom = this.denom * other.denom
        return Quotient(newNumer to newDenom)
    }

    override operator fun unaryMinus(): Quotient {
        return Quotient(-numer to denom)
    }
}