package expressions.binary

import equations.Domain
import equations.FullDomain
import equations.equateTo
import expressions.*
import expressions.longs.Product
import expressions.number.Rational
import expressions.number.Real
import expressions.number.power
import expressions.number.toRational

class Quotient (
    body: Pair<Expression, Expression>,
    domain: Domain = FullDomain
) : BinaryExpression(body, domain, false) {
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
        sBody.removeIrrationalityInDenom().apply { if (this != null) return Quotient(first to second).simplifySoftly() }

        val (sNumer, sDenom) = sBody
        if (!sDenom.isNumber) addDomainRestriction(sDenom equateTo zero())
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
                newNumerBody.add(it.base.power(unit() - it.exponent))
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

    override fun _commonFactor(other: Expression): Expression {
        return commonFactor(numer, other)
    }

    override fun _reduceOrNull(other: Expression): Expression? {
        val reducedNumer = numer.reduceOrNull(other) ?: return null
        return Quotient(reducedNumer to denom)
    }

    override fun rationalPart(): Rational {
        assert(final)
        return numer.rationalPart()
    }
    override fun nonRationalPart(): Expression {
        assert(final)
        return Quotient(numer.nonRationalPart() to denom).apply { final = true }
    }
    override fun numericalPart(): Expression {
        assert(final)
        val numerNumPart = numer.numericalPart()
        val denomNumPart = denom.numericalPart()
        return if (denomNumPart.isUnitRational()) numerNumPart
               else                               Quotient(numerNumPart to denomNumPart).apply { final = true }
    }
    override fun nonNumericalPart(): Expression {
        assert(final)
        val numerNonNumPart = numer.nonNumericalPart()
        val denomNonNumPart = denom.nonNumericalPart()
        return if (denomNonNumPart.isUnitRational()) numerNonNumPart
               else                                  Quotient(numerNonNumPart to denomNonNumPart).apply { final = true }
    }

    fun sumAsQuotient(other: Quotient): Quotient {
        val newNumer = this.numer * other.denom + other.numer * this.denom
        val newDenom = this.denom * other.denom
        return Quotient(newNumer to newDenom)
    }

    operator fun times(other: Quotient): Quotient {
        val newNumer = this.numer * other.numer
        val newDenom = this.denom * other.denom
        return Quotient(newNumer to newDenom)
    }

    override operator fun unaryMinus(): Quotient {
        return Quotient(-numer to denom)
    }
}