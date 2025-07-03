package expressions.number

import equations.Domain
import equations.FullDomain
import expressions.*
import utils.gcd
import utils.power
import kotlin.math.abs

class Rational (
    override val body: Pair<Int, Int>,
    domain: Domain = FullDomain,
    final: Boolean = false
) : Expression(domain, final) {
    val numer = body.first
    val denom = body.second

    init { assert(denom != 0) }

    override fun _isNumber() = true
    override fun rationalPart() = this
    override fun nonRationalPart() = unit()

    override fun simplify() = super.simplify() as Rational
    override fun _simplify(): Rational {
        if (isZero()) return zero()

        val gcd = gcd(numer, denom)
        val newNumer = (if (denom < 0) -numer else numer) / gcd
        val newDenom = abs(denom) / gcd
        return newNumer over newDenom
    }

    override fun _commonFactor(other: Expression): Rational? {
        if (other !is Rational) return null
        val commonDenom = this.denom * other.denom
        val thisNumer = this.numer * other.denom
        val otherNumer = this.denom * other.numer
        val cfAbs = gcd(thisNumer, otherNumer) over commonDenom
        return if (this.isNegative() && other.isNegative()) -cfAbs else cfAbs
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


infix fun Int.over(other: Int) = Rational(this to other)
fun Int.toRational() = Rational(this to 1, final=true)

fun min(a: Rational, b: Rational): Rational = if ((a - b).isNegative()) a else b