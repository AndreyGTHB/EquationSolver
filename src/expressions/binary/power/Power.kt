package expressions.binary.power

import expressions.Expression
import expressions.binary.BinaryExpression
import expressions.number.Numerical
import expressions.number.Rational

open class Power protected constructor(body: Pair<Expression, Expression>, final: Boolean) : BinaryExpression(body, final) {
    constructor(body: Pair<Expression, Expression>) : this(body, false)

    val base = body.first
    open val exponent = body.second

    override fun simplify(): Expression {
        if (final) return this

        val sPower = simplifySoftly()
        val (sBase, sExponent) = sPower.body
        return if (sExponent is Rational) RationalPower(sBase to sExponent).simplify()
               else                               Power(sBase to sExponent, true)
    }
    protected open fun simplifySoftly(): Power {
        if (final) return this

        var (sBase, sExponent) = simplifyBody()
        if (sBase is Power) {
            sExponent = (sExponent * sBase.exponent).simplify()
            sBase = sBase.base
        }
        return Power(sBase to sExponent)
    }
}