package expressions.binary

import expressions.Expression
import expressions.number.Rational
import expressions.commonFactor

class Quotient private constructor(body: Pair<Expression, Expression>, final: Boolean) : BinaryExpression(body, final) {
    constructor(body: Pair<Expression, Expression>) : this(body, false)

    val numer = body.first
    val denom = body.second

    override fun simplify(): Expression {
        if (final) return this

        val sQuotient = simplifySoftly()
        val (sNumer, sDenom) = sQuotient.body
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
        if (final) return this

        val (simpleNumer, simpleDenom) = simplifyBody()
        val cf = commonFactor(simpleNumer, simpleDenom)
        val reducedNumer = simpleNumer.reduce(cf)
        val reducedDenom = simpleDenom.reduce(cf)
        return Quotient(reducedNumer to reducedDenom, true)
    }

    override fun commonFactor(other: Expression): Expression {
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
        return Quotient(numer.nonRationalPart() to denom, true)
    }
    override fun numericalPart(): Expression {
        if (!final) TODO("Must be simplified")
        val numerNumPart = numer.numericalPart()
        val denomNumPart = denom.numericalPart()
        return if (denomNumPart.isUnitRational()) numerNumPart
          else                                    Quotient(numerNumPart to denomNumPart, true)
    }
    override fun nonNumericalPart(): Expression {
        if (!final) TODO("Must be simplified")
        val numerNonNumPart = numer.nonNumericalPart()
        val denomNonNumPart = denom.nonNumericalPart()
        return if (denomNonNumPart.isUnitRational()) numerNonNumPart
        else                                         Quotient(numerNonNumPart to denomNonNumPart, true)
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