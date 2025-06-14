package expressions.binary

import expressions.Expression
import expressions.commonFactor
import expressions.longs.Product
import expressions.monomials.Monomial
import expressions.number.Rational
import expressions.number.Real
import expressions.unit
import utils.min

class Power private constructor(body: Pair<Expression, Expression>, final: Boolean) : BinaryExpression(body, final) {
    constructor(body: Pair<Expression, Expression>) : this(body, false)

    val base = body.first
    val exponent = body.second

    override suspend fun simplify(): Expression {
        if (final) return this

        val sPower = simplifySoftly()
        val (sBase, sExponent) = sPower.body

        if (sBase is Product) {
            val productBody = sBase.body.map { Power(it to sExponent) }
            return Product(productBody).simplify()
        }
        if (sBase is Quotient) {
            val quotientBody = Power(sBase.numer to sExponent) to Power(sBase.denom to sExponent)
            return Quotient(quotientBody).simplify()
        }

        return if (sExponent is Rational) sPower.simplifyAsRationalPower()
               else                       sPower
    }
    private suspend fun simplifySoftly(): Power {
        var (sBase, sExponent) = simplifyBody()
        if (sBase is Power) {
            sExponent = (sExponent * sBase.exponent).simplify()
            sBase = sBase.base
        }
        return Power(sBase to sExponent, true)
    }
    private suspend fun simplifyAsRationalPower(): Expression {
        exponent as Rational
        if (exponent.isNegative()) return (unit() / Power(base to -exponent)).simplify()
        if (exponent.isZero())     return unit()
        if (exponent.isUnit())     return base

        return when (base) {
            is Rational -> {
                val asQuotient = Real(base.numer to exponent) / Real(base.denom to exponent)
                asQuotient.simplify()
            }
            is Real -> {
                val asReal = Real(base.base to base.exponent * exponent)
                asReal.simplify()
            }
            is Monomial -> base.power(exponent).simplify()
            else -> {
                if (exponent.isInteger()) {
                    val asProduct = Product(List(exponent.numer) { base })
                    asProduct.simplify()
                }
                else this
            }
        }
    }

    override suspend fun commonFactor(other: Expression): Expression {
        if (other is Power) return commonFactorWithPower(other)
        val cfWithBase = commonFactor(base, other)
        return Power(cfWithBase to exponent)
    }
    private fun commonFactorWithPower(other: Power): Expression {
        val cfOfBases = commonFactor(this.base, other.base)
        return if (this.exponent is Rational && other.exponent is Rational) {
            Power(cfOfBases to min(this.exponent, other.exponent))
        }
        else cfOfBases
    }
}