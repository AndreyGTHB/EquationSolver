package expressions.binary.power

import expressions.Expression
import expressions.monomials.Monomial
import expressions.numerical.Rational

class RationalPower private constructor(override val body: Pair<Expression, Rational>, final: Boolean) : Power(body, final) {
    constructor(body: Pair<Expression, Rational>) : this(body, false)

    override val exponent = body.second

    override fun simplify(): Expression {
        val simpleThis = simplifySoftly()
        val (simpleBase, simpleExponent) = simpleThis.body
        if (simpleBase.isZeroRational()) TODO("Zero exponentiation")
        if (simpleBase.isUnitRational() || simpleExponent.isNull()) return simpleBase

        if (simpleBase is Monomial) TODO("To implement")
        return simpleThis
    }
    override fun simplifySoftly(): RationalPower {
        val (simpleBase, simpleExponent) = super.simplifySoftly().body
        simpleExponent as Rational
        return RationalPower(simpleBase to simpleExponent, true)
    }

    private fun Monomial.pow(exp: Rational): Monomial {
        TODO("To implement")
    }
}