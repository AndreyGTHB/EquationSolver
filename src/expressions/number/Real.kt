package expressions.number

import expressions.Expression
import expressions.binary.power.RationalPower
import utils.factorise
import utils.gcd

class Real private constructor(override val body: Pair<Rational, Rational>, final: Boolean) : RationalPower(body, final) {
    constructor(body: Pair<Rational, Rational>) : this(body, false)

    override val base = body.first

    fun simplify(): Expression {}
    fun simplifySoftly(): Real {
        val (simpleBase, simpleExponent) = simplifyBody()
        val greatestFullNumerExponent = simpleBase.numer.factorise().values.gcd()
        val greatestFullDenomExponent = simpleBase.denom.factorise().values.gcd()
    }

    override fun simplifyBody(): Pair<Rational, Rational> = base.simplify() to exponent.simplify()

    override fun toString(): String = "($base)^($exponent)"
}