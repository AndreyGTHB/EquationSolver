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
        get() = gcd(numer, denom) == 1

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
        if (newNumer == 0) return zero()
        return newNumer over newDenom
    }

    override fun _reduceOrNull(other: Expression): Rational? = if (other is Rational) this / other
                                                               else                   null

    fun flip() = denom over numer

    fun isZero() = numer == 0
    fun isUnit() = numer == denom
    fun isInteger() = numer % denom == 0
//    override fun equals(other: Any?): Boolean {
//        other ?: return false
//        return when (other) {
//            is Rational -> equalsToFraction(other)
//            is Int -> equalsToFraction(other.toRational())
//            else -> false
//        }
//    }
//    private fun equalsToFraction(other: Rational): Boolean {
//        val (thisNumer, thisDenom) = this.simplify().body
//        val (otherNumer, otherDenom) = other.simplify().body
//        return thisNumer == otherNumer && thisDenom == otherDenom
//    }

    fun isPositive(): Boolean = numer * denom > 0
    fun isNegative(): Boolean = numer * denom < 0

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

    infix fun raisedTo(exp: Int): Rational = if (exp >= 0) (numer.power(exp) over denom.power(exp))
                                             else          flip() raisedTo (-exp)

    override operator fun unaryMinus() = -numer over denom

    operator fun inc(): Rational {
        return this + 1
    }

    override fun toString(): String = "$numer/$denom"
    fun toFloat(): Float = numer.toFloat() / denom
}