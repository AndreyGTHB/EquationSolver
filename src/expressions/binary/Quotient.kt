package expressions.binary

import equations.Domain
import equations.FullDomain
import equations.equateTo
import expressions.*
import expressions.number.Rational

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
        val (sNumer, sDenom) = simplifyBody()
        if (!sDenom.isNumber()) addDomainRestriction(sDenom equateTo zero())
        if (sNumer.isZeroRational()) return zeroQuotient()

        val cf = commonFactor(sNumer, sDenom)
        val reducedNumer = sNumer.reduce(cf)
        val reducedDenom = sDenom.reduce(cf)
        return Quotient(reducedNumer to reducedDenom)
    }

    override fun _commonFactor(other: Expression): Expression {
        return commonFactor(numer, other)
    }

    override fun _reduceOrNull(other: Expression): Expression? {
        val reducedNumer = numer.reduceOrNull(other) ?: return null
        return Quotient(reducedNumer to denom)
    }

    override fun rationalPart(): Rational {
        if (!final) TODO("Must be simplified")
        return numer.rationalPart()
    }
    override fun nonRationalPart(): Expression {
        if (!final) TODO("Must be simplified")
        return Quotient(numer.nonRationalPart() to denom).apply { final = true }
    }
    override fun numericalPart(): Expression {
        if (!final) TODO("Must be simplified")
        val numerNumPart = numer.numericalPart()
        val denomNumPart = denom.numericalPart()
        return if (denomNumPart.isUnitRational()) numerNumPart
          else                                    Quotient(numerNumPart to denomNumPart).apply { final = true }
    }
    override fun nonNumericalPart(): Expression {
        if (!final) TODO("Must be simplified")
        val numerNonNumPart = numer.nonNumericalPart()
        val denomNonNumPart = denom.nonNumericalPart()
        return if (denomNonNumPart.isUnitRational()) numerNonNumPart
               else Quotient(numerNonNumPart to denomNonNumPart).apply { final = true }
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