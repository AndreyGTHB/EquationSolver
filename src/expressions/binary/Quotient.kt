package expressions.binary

import equations.Equation
import expressions.*
import expressions.longs.Product
import expressions.number.Rational
import expressions.number.Real
import expressions.number.power
import expressions.number.toRational

class Quotient (
    body: Pair<Expression, Expression>,
) : BinaryExpression(body, final=false) {
    val numer = body.first
    val denom = body.second

    override fun _simplify(): Expression {
        val sQuotient = simplifySoftly()
        val (sNumer, sDenom) = sQuotient.body
        if (sDenom.isZeroRational()) return InvalidExpression
        if (sDenom.isUnitRational()) return sNumer

        if (sNumer is Quotient && sDenom is Quotient) {
            val qt = (sNumer.numer * sDenom.denom) / (sNumer.denom * sDenom.numer)
            return qt.simplify()
        }
        if (sNumer is Quotient) {
            val qt = sNumer.numer / (sNumer.denom * sDenom)
            return qt.simplify()
        }
        if (sDenom is Quotient) {
            val qt = (sNumer * sDenom.denom) / sDenom.numer
            return qt.simplify()
        }

        val denomRationalPart = sDenom.rationalPart()
        if (!denomRationalPart.isUnitRational()) {
            val denomNonRationalPart = sDenom.nonRationalPart()
            val qt = (sNumer * denomRationalPart.flip()) / denomNonRationalPart
            return qt.simplify()
        }

        return sQuotient
    }

    private fun simplifySoftly(): Quotient {
        val sBody = simplifyBody()
        sBody.removeIrrationalityInDenom()?.apply { return Quotient(first to second).simplifySoftly() }

        val (sNumer, sDenom) = sBody
        if (!sDenom.isNumber) {
            val newConstraint = Equation(sDenom to zero()).solveIgnoringDomain()
            addConstraints(-newConstraint)
        }
        if (sNumer.isZeroRational()) return zeroQuotient()

        val cf = commonFactor(sNumer, sDenom)
        val reducedNumer = sNumer.reduce(cf)
        val reducedDenom = sDenom.reduce(cf)
        return Quotient(reducedNumer to reducedDenom)
    }

    private fun Pair<Expression, Expression>.removeIrrationalityInDenom(): Pair<Expression, Expression>? {
        val newNumerBody = first.asProduct().body.toMutableList()
        val newDenomBody = mutableListOf<Expression>()
        var changed = false
        second.asProduct().body.forEach {
            if (it is Real) {
                newNumerBody.add(it.base.power(one() - it.exponent))
                newDenomBody.add(it.base.toRational())
                changed = true
            }
            else newDenomBody.add(it)
        }

        if (!changed) return null
        val newNumer = if (newNumerBody.size > 1) Product(newNumerBody) else newNumerBody[0]
        val newDenom = if (newDenomBody.size > 1) Product(newDenomBody) else newDenomBody[0]
        return newNumer to newDenom
    }

    override fun _substitute(variable: Char, value: Expression) = Quotient(substituteIntoBody(variable, value))

    override fun _commonFactor(other: Expression): Expression {
        return commonFactor(numer, other)
    }

    override fun _reduceOrNull(other: Expression): Expression? {
        val reducedNumer = numer.reduceOrNull(other) ?: return null
        return Quotient(reducedNumer to denom)
    }

    override fun _rationalPart(): Rational {
        assert(final)
        return numer._rationalPart()
    }
    override fun _nonRationalPart(): Expression {
        assert(final)
        return Quotient(numer._nonRationalPart() to denom).apply { final = true }
    }
    override fun _numericalPart(): Expression {
        assert(final)
        val numerNumPart = numer._numericalPart()
        val denomNumPart = denom._numericalPart()
        return if (denomNumPart.isUnitRational()) numerNumPart
               else                               Quotient(numerNumPart to denomNumPart).apply { final = true }
    }
    override fun _nonNumericalPart(): Expression {
        assert(final)
        val numerNonNumPart = numer._nonNumericalPart()
        val denomNonNumPart = denom._nonNumericalPart()
        return if (denomNonNumPart.isUnitRational()) numerNonNumPart
               else                                  Quotient(numerNonNumPart to denomNonNumPart).apply { final = true }
    }

    operator fun times(other: Quotient): Quotient {
        val newNumer = this.numer * other.numer
        val newDenom = this.denom * other.denom
        return Quotient(newNumer to newDenom)
    }

    override fun unaryMinus(): Quotient {
        return Quotient(-numer to denom)
    }
}