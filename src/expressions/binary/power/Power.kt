package expressions.binary.power

import expressions.Expression
import expressions.binary.BinaryExpression
import expressions.number.Rational

open class Power protected constructor(body: Pair<Expression, Expression>, final: Boolean) : BinaryExpression(body, final) {
    constructor(body: Pair<Expression, Expression>) : this(body, false)

    open val base = body.first
    open val exponent = body.second

    override fun simplify(): Expression {
        if (final) return this

        val (simpleBase, simpleExponent) = simplifySoftly().body
        return if (simpleExponent is Rational) RationalPower(simpleBase to simpleExponent).simplify()
               else                                    Power(simpleBase to simpleExponent, true)
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