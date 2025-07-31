package expressions.binary

import ch.obermuhlner.math.big.BigDecimalMath.pow
import expressions.Expression
import expressions.isZeroRational
import expressions.longs.Product
import expressions.monomials.Monomial
import expressions.number.Rational
import expressions.number.Real
import expressions.number.calcDelta
import expressions.number.min
import expressions.one
import expressions.zero
import java.math.BigDecimal

class Power (
    body: Pair<Expression, Expression>,
) : BinaryExpression(body, final=false) {
    val base = body.first
    val exponent = body.second

    override fun _simplify(): Expression {
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
    private fun simplifySoftly(): Power {
        var (sBase, sExponent) = simplifyBody()
        if (sBase is Power) {
            sExponent = (sExponent * sBase.exponent).simplify()
            sBase = sBase.base
        }
        return Power(sBase to sExponent)
    }
    private fun simplifyAsRationalPower(): Expression {
        exponent as Rational
        if (exponent.isNegative()) return (one() / Power(base to -exponent)).simplify()
        if (exponent.isZero())     return one()
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

    override fun _substitute(variable: Char, value: Expression) = Power(substituteIntoBody(variable, value))

    override fun _commonFactor(other: Expression): Expression? {
        return if (other is Power) return commonFactorWithPower(other)
          else                            commonFactor(base, other)
    }
    private fun commonFactorWithPower(other: Power): Expression? {
        val cfOfBases = commonFactor(this.base, other.base)
        return if (this.exponent is Rational && other.exponent is Rational) {
            Power(cfOfBases to min(this.exponent, other.exponent))
        }
        else null
    }

    override fun _approx(scale: Int): BigDecimal {
        return if (base > zero())         approxWithPositiveBase(scale)
          else if (base.isZeroRational()) approxWithZeroBase(scale)
          else                            approxWithNegativeBase(scale)
    }

    private fun approxWithPositiveBase(scale: Int): BigDecimal {
        val baseB = base.upperBound()
        val expB = exponent.upperBound()
        val requiredDelta = calcDelta(scale)
        var subScale = scale
        do {
            val epsilon = calcDelta(subScale)
            val currDelta = pow(baseB + epsilon, expB + epsilon, MathContext())
        }
    }

    private fun approxWithZeroBase(scale: Int): BigDecimal {
        TODO()
    }

    private fun approxWithNegativeBase(scale: Int): BigDecimal { TODO() }

    override fun _reduceOrNull(other: Expression) = null
}
