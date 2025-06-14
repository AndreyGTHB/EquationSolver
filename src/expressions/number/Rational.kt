package expressions.number

import expressions.Expression
import expressions.unit
import expressions.zero
import utils.gcd
import utils.over
import utils.power
import kotlin.math.abs

class Rational (override val body: Pair<Int, Int>) : Expression() {
    override val final: Boolean
        get() = (denom >= 0 && gcd(numer, denom) == 1)

    val numer = body.first
    val denom = body.second

    init { if (denom == 0) throw RuntimeException("Zero division") }

    override fun _isNumber() = true
    override fun rationalPart() = this
    override fun nonRationalPart() = unit()
    override fun numericalPart() = this
    override fun nonNumericalPart() = unit()

    override fun simplify(): Rational {
        if (final) return this
        return simplifySoftly()
    }
    private fun simplifySoftly(): Rational {
        if (isZero()) return zero()

        val gcd = gcd(numer, denom)
        val newNumer = (if (denom < 0) -numer else numer) / gcd
        val newDenom = abs(denom) / gcd
        return newNumer over newDenom
    }

    override fun _reduceOrNull(other: Expression): Rational? = if (other is Rational) this / other
                                                               else                   null

    override fun compareTo(other: Expression): Int {
        return if (other is Rational) this.numer * other.denom - other.numer * this.denom
          else                        super.compareTo(other)
    }

    fun flip() = denom over numer

    fun isZero() = numer == 0
    fun isUnit() = numer == denom
    fun isInteger() = numer % denom == 0

    fun isPositive() = numer * denom > 0
    fun isNegative() = numer * denom < 0

    operator fun plus(other: Int) = numer + other * denom over denom
    operator fun plus(other: Rational): Rational {
        val newN = this.numer * other.denom + other.numer * this.denom
        val newD = this.denom * other.denom
        return newN over newD
    }

    operator fun minus(other: Int) = numer - other * denom over denom
    operator fun minus(other: Rational): Rational {
        val newN = this.numer * other.denom - other.numer * this.denom
        val newD = this.denom * other.denom
        return newN over newD
    }

    operator fun times(other: Int) = numer * other over denom
    operator fun times(other: Rational) = this.numer * other.numer over this.denom * other.denom

    operator fun div(other: Int) = numer over denom * other
    operator fun div(other: Rational) = this.numer * other.denom over this.denom * other.numer

    fun power(exp: Int): Rational = if (exp >= 0) (numer.power(exp) over denom.power(exp))
                                    else          flip().power(-exp)
    infix fun raisedTo(exp: Int): Rational = power(exp)

    override operator fun unaryMinus() = -numer over denom

    operator fun inc(): Rational {
        return this + 1
    }

    override fun toString(): String = "$numer/$denom"
    fun toFloat(): Float = numer.toFloat() / denom
}