package expressions.binary

import expressions.Expression
import expressions.number.Rational
import expressions.commonFactor
import expressions.longs.Product
import expressions.unit

class Quotient private constructor(body: Pair<Expression, Expression>, final: Boolean) : BinaryExpression(body, final) {
    constructor(body: Pair<Expression, Expression>) : this(body, false)

    val numer = body.first
    val denom = body.second

    override fun simplify(): Expression {
        if (final) return this

        val simpleThis = simplifySoftly()
        if (simpleThis.denom is Rational) return simpleThis.numer.reduce(simpleThis.denom)
        return Quotient(simpleThis.body, true)
    }
    private fun simplifySoftly(): Quotient {
        if (final) return this

        val (simpleNumer, simpleDenom) = simplifyBody()
        val cf = commonFactor(simpleNumer, simpleDenom)
        val reducedNumer = simpleNumer.reduce(cf)
        val reducedDenom = simpleDenom.reduce(cf)
        return Quotient(reducedNumer to reducedDenom)
    }

    override fun commonFactor(other: Expression): Expression {
        return commonFactor(numer, other)
    }

    override fun _reduceOrNull(other: Expression): Expression? {
        val reducedNumer = numer.reduceOrNull(other) ?: return null
        return Quotient(reducedNumer to denom)
    }

    fun rationalPart(): Rational {
        if (!final) TODO("Must be simplified")
        return when (numer) {
            is Rational -> numer
            is Product  -> numer.rationalPart()
            else        -> unit()
        }
    }
    fun nonRationalPart(): Expression {
        if (!final) TODO("Must be simplified")
        return when (numer) {
            is Rational -> Quotient(unit() to denom, true)
            is Product  -> numer.nonRationalPart()
            else        -> this
        }
    }
    fun numericalPart(): Expression {
        if (!final) TODO("Must be simplified")
        val numerNumPart = if (numer.isNumber()) numer
                           else if (numer is Product) numer.numericalPart()
                           else unit()
        val denomNumPart = if (denom.isNumber()) denom
                           else if (denom is Product) denom.numericalPart()
                           else unit()
        return if (denomNumPart.isUnitRational()) numerNumPart
          else                                    Quotient(numerNumPart to denomNumPart, true)
    }
    fun nonNumericalPart(): Expression {
        if (!final) TODO("Must be simplified")
        val numerNonNumPart = if (numer.isNumber()) unit()
                              else if (numer is Product) numer.nonNumericalPart()
                              else                       numer
        val denomNonNumPart = if (denom.isNumber()) unit()
                              else if (denom is Product) denom.nonNumericalPart()
                              else                       denom
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