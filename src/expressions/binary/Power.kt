package expressions.binary

import expressions.Expression
import expressions.monomials.Monomial
import expressions.numerical.Rational
import expressions.unit

class Power private constructor(body: Pair<Expression, Expression>, final: Boolean) : BinaryExpression(body, final) {
    constructor(body: Pair<Expression, Expression>) : this(body, false)

    val base = body.first
    val exponent = body.second

    override fun simplify(): Expression {
        if (final) return this

        val (simpleBase, simpleExponent) = simplifySoftly().body

        if (simpleBase.isZeroRational()) TODO("Zero as a power base")
        if (simpleBase.isUnitRational() || simpleExponent.isUnitRational()) return simpleBase
        if (simpleExponent.isZeroRational()) return unit()

        if (simpleBase is Monomial && simpleExponent is Rational) {
            val newCoeff = simpleBase.coeff ^ simpleExponent
        }

        return Power(simpleBase to simpleExponent, true)
    }
    override fun simplifySoftly(): Power {
        if (final) return this

        var (simpleBase, simpleExponent) = simplifyBody()
        if (simpleBase is Power) {
            simpleExponent = (simpleExponent * simpleBase.exponent).simplify()
            simpleBase = simpleBase.base
        }
        return Power(simpleBase to simpleExponent)
    }
}