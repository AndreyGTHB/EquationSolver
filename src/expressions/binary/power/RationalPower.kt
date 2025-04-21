package expressions.binary.power

import expressions.Expression
import expressions.monomials.Monomial
import expressions.number.Rational

open class RationalPower protected constructor(override val body: Pair<Expression, Rational>, final: Boolean) : Power(body, final) {
    constructor(body: Pair<Expression, Rational>) : this(body, false)

    override val exponent = body.second

    override fun simplify(): Expression {
        if (final) return this

        val (simpleBase, simpleExponent) = simplifySoftly().body
        if (simpleBase.isZeroRational()) TODO("Zero exponentiation")
        if (simpleBase.isUnitRational() || simpleExponent.isNull()) return simpleBase

        val result = if (simpleBase is Monomial) simpleBase.raise(simpleExponent).simplify()
                     else                        RationalPower(simpleBase to simpleExponent, true)
        return result
    }
    override fun simplifySoftly(): RationalPower {
        if (final) return this

        val (simpleBase, simpleExponent) = super.simplifySoftly().body
        simpleExponent as Rational
        return RationalPower(simpleBase to simpleExponent)
    }
}