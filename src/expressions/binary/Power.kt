package expressions.binary

import ch.obermuhlner.math.big.BigDecimalMath.pow
import expressions.Expression
import expressions.isUnitRational
import expressions.isZeroRational
import expressions.longs.Product
import expressions.longs.Sum
import expressions.monomials.Monomial
import expressions.number.Rational
import expressions.number.Real
import expressions.number.calcDelta
import expressions.number.min
import expressions.one
import utils.numberOfDigits
import utils.rescale
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode

class Power (
    body: Pair<Expression, Expression>,
    final: Boolean = false
) : BinaryExpression(body, final=final) {
    val base = body.first
    val exponent = body.second

    override fun _simplify(): Expression {
        val sPower = simplifySoftly()
        val (sBase, sExponent) = sPower.body

        when (sBase) {
            is Product -> {
                val productBody = sBase.body.map { Power(it to sExponent) }
                return Product(productBody).simplify()
            }
            is Quotient -> {
                val quotientBody = Power(sBase.numer to sExponent) to Power(sBase.denom to sExponent)
                return Quotient(quotientBody).simplify()
            }
            is Sum -> {
                val (cif, reducedBase) = sBase.separatedWithCommonInternalFactor
                if (!cif.isUnitRational()) {
                    val productBody = listOf(cif.raisedTo(sExponent), reducedBase.raisedTo(sExponent))
                    return Product(productBody).simplify()
                }
            }
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
        return if (exponent !is Rational) takeIf { this == other }
          else if (other is Power)        commonFactorWithPower(other)
          else                            commonFactor(base, other).raisedTo(min(one(), exponent))
    }
    private fun commonFactorWithPower(other: Power): Expression? {
        exponent as Rational
        return if (other.exponent is Rational) {
            val cfOfBases = commonFactor(this.base, other.base)
            Power(cfOfBases to min(this.exponent, other.exponent))
        }
        else null
    }

    override fun _reduceOrNull(other: Expression): Expression? {
        return if (exponent !is Rational) null
               else when (other) {
            is Power -> reduceByPower(other)
            else     -> reduceAsRationalPower(other)
        }
    }

    private fun reduceByPower(other: Power): Expression? {
        if (other.exponent !is Rational || this.exponent < other.exponent) return null

        val reducedBase = this.base.reduceOrNull(other.base) ?: return null
        return reducedBase.raisedTo(exponent) * other.base.raisedTo(this.exponent - other.exponent)
    }

    private fun reduceAsRationalPower(other: Expression): Expression? {
        exponent as Rational
        if (other !is Rational && exponent < one()) return null

        val reducedBase = base.reduceOrNull(other) ?: return null
        return reducedBase.raisedTo(exponent) * other.raisedTo(exponent - one())
    }

    override fun _approx(scale: Int): BigDecimal {
        return if (true)         approxWithPositiveBase(scale) // Realise others
          else if (base.isZeroRational()) approxWithZeroBase(scale)
          else                            approxWithNegativeBase(scale)
    }

    private fun approxWithPositiveBase(scale: Int): BigDecimal {
        val baseB = base.upperBound()
        val expB = exponent.upperBound()
        val boundPrecision = if (expB >= BigDecimal.ZERO) baseB.toBigIntegerExact().numberOfDigits() * expB.toInt() + 1
                             else                         scale
        val thisB = pow(baseB, expB, MathContext(boundPrecision, RoundingMode.DOWN)).rescale()
        val thisBLength = if (expB >= BigDecimal.ZERO ) thisB.toBigIntegerExact().numberOfDigits()
                          else                          0

        val requiredDelta = calcDelta(scale)
        var subScale = scale
        val precision = (thisBLength + scale + 2).takeIf { it >= 1 } ?: 1
        do {
            subScale += 2
            val epsilon = calcDelta(subScale)
            val currDelta = pow(baseB + epsilon, expB + epsilon, MathContext(precision, RoundingMode.UP)) - thisB
        } while (currDelta >= requiredDelta)
        return pow(
            base.approx(subScale),
            exponent.approx(subScale),
            MathContext(precision)
        ).setScale(scale, RoundingMode.HALF_UP)
    }

    private fun approxWithZeroBase(scale: Int): BigDecimal {
        TODO()
    }

    private fun approxWithNegativeBase(scale: Int): BigDecimal { TODO() }
}
