package expressions.binary.power

import expressions.Expression
import expressions.monomials.Monomial
import expressions.numerical.Rational

class RationalPower private constructor(override val body: Pair<Expression, Rational>, final: Boolean) : Power(body, final) {
    constructor(body: Pair<Expression, Rational>) : this(body, false)

    override val exponent = body.second

    override fun simplify(): Expression {
        val (simpleBase, simpleExponent) = simplifySoftly().body
        if (simpleBase.isZeroRational()) TODO("Zero exponentiation")
        if (simpleBase.isUnitRational() || simpleExponent.isNull()) return simpleBase

        if (simpleBase is Monomial) {}
        return RationalPower(simpleBase to simpleExponent, true)
    }
    override fun simplifySoftly(): RationalPower {
        val (simpleBase, simpleExponent) = super.simplifySoftly().body
        simpleExponent as Rational
        return RationalPower(simpleBase to simpleExponent)
    }
}