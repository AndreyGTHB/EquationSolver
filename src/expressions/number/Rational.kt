package expressions.number

import expressions.Expression
import expressions.zero
import utils.GCD
import utils.over
import kotlin.math.abs

class Rational (override val body: Pair<Int, Int>) : Expression() {
    override val final: Boolean
        get() = GCD(numer, denom) == 1

    val numer = body.first
    val denom = body.second

    init {
        if (denom == 0) { throw RuntimeException("Zero division") }
    }

    override fun simplify(): Rational {
        if (final) return this
        return simplifySoftly()
    }
    override fun simplifySoftly(): Rational {
        if (isNull()) return zero()

        val gcd = GCD(numer, denom)
        val newNumer = (if (denom < 0) -numer else numer) / gcd
        val newDenom = abs(denom) / gcd
        if (newNumer == 0) return zero()
        return newNumer over newDenom
    }

    override fun reduceOrNull(other: Expression): Rational? {
        if (other is Rational) return (this / other).simplify()
        return null
    }

    fun flip(): Rational = Rational(denom to numer)

    fun isNull(): Boolean = numer == 0
    fun isUnit(): Boolean = numer == denom
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

    override operator fun unaryMinus() = -numer over denom

    operator fun inc(): Rational {
        return this + 1
    }

    override fun toString(): String = "$numer/$denom"
    fun toFloat(): Float = numer.toFloat() / denom
}