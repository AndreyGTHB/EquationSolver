package expressions.binary

import expressions.Expression
import expressions.numerical.Fraction
import expressions.commonFactor

class Quotient private constructor(body: Pair<Expression, Expression>, final: Boolean) : BinaryExpression(body, final) {
    constructor(body: Pair<Expression, Expression>) : this(body, false)

    val numer = body.first
    val denom = body.second

    override fun simplify(): Expression {
        if (final) return this

        val simpleThis = simplifySoftly()
        if (simpleThis.denom is Fraction) return simpleThis.numer.reduceOrNull(simpleThis.denom)!!
        return simpleThis
    }
    override fun simplifySoftly(): Quotient {
        if (final) return this

        val (simpleNumer, simpleDenom) = simplifyBody()
        val cf = commonFactor(simpleNumer, simpleDenom)
        val reducedNumer = simpleNumer.reduceOrNull(cf)!!
        val reducedDenom = simpleDenom.reduceOrNull(cf)!!
        return Quotient(reducedNumer to reducedDenom, true)
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

    override operator fun unaryMinus(): Quotient {
        return Quotient(-numer to denom)
    }
}