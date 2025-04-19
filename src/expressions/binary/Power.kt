package expressions.binary

import expressions.Expression
import expressions.Variable
import expressions.commonFactor
import expressions.unit

class Power private constructor(body: Pair<Expression, Expression>, final: Boolean) : BinaryExpression(body, final) {
    constructor(body: Pair<Expression, Expression>) : this(body, false)

    val base = body.first
    val exponent = body.second

    override fun simplify(): Expression {
        if (final) return this

        val (simpleBase, simpleExponent) = simplifySoftly().body
        if (simpleBase.isZeroRational()
            || simpleBase.isUnitRational()
            || simpleExponent.isUnitRational()) return simpleBase
        if (simpleExponent.isZeroRational()) return unit()
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

    override fun commonFactor(other: Expression): Expression? {
        return when (other) {
            is Variable -> commonFactorWithVariable(other)
            is Power -> commonFactorWithPower(other)
        }
    }
    private fun commonFactorWithVariable(other: Variable) = commonFactor(base, other)
    private fun commonFactorWithPower(other: Power) {
        val commonBaseFactor = commonFactor(this.base, other.base)
        TODO()
//        return Power(commonBaseFactor to min)
    }
}