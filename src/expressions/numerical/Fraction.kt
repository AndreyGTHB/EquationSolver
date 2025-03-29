package expressions.numerical

import expressions.Expression
import expressions.longs.Product
import utils.GCD
import utils.toFraction
import kotlin.math.abs

class Fraction private constructor(override val body: Pair<Int, Int>, final: Boolean) : Expression(final) {
    constructor(body: Pair<Int, Int>) : this(body, false)
    override val final: Boolean
        get() = GCD(numer, denom) == 1

    companion object {
        fun nullFraction(): Fraction = Fraction(0 to 1, true)
    }

    val numer = body.first
    val denom = body.second

    init {
        if (denom == 0) { throw RuntimeException("Zero division") }
    }

    override fun simplify(): Fraction {
        if (final) return this
        return simplifySoftly()
    }
    override fun simplifySoftly(): Fraction {
        if (isNull()) return nullFraction()

        val gcd = GCD(numer, denom)
        val newNumer = (if (denom < 0) -numer else numer) / gcd
        val newDenom = abs(denom) / gcd
        return Fraction(newNumer to newDenom, true)
    }

    override fun commonFactor(other: Expression): Fraction? {
        if (other is Fraction && other.isNull()) {
            if (this.isNull()) throw RuntimeException("Two zeros")
            return this
        }
        return null
    }
    override fun reduceOrNull(other: Expression): Fraction? {
        if (isNull()) return this
        if (other is Fraction) return (this / other).simplify()
        return null
    }

    fun isNull(): Boolean = numer == 0
    fun flip(): Fraction = Fraction(denom to numer)

    operator fun plus(other: Int): Fraction {
        return Fraction(numer + other * denom to denom)
    }
    operator fun plus(other: Fraction): Fraction {
        val newN = this.numer * other.denom + other.numer * this.denom
        val newD = this.denom * other.denom
        return Fraction(newN to newD)
    }

    operator fun minus(other: Int): Fraction {
        return Fraction(numer - other * denom to denom)
    }
    operator fun minus(other: Fraction): Fraction {
        val newN = this.numer * other.denom - other.numer * this.denom
        val newD = this.denom * other.denom
        return Fraction(newN to newD)
    }

    operator fun times(other: Int): Fraction {
        return Fraction(numer * other to denom)
    }
    operator fun times(other: Fraction): Fraction {
        return Fraction(this.numer * other.numer to this.denom * other.denom)
    }

    operator fun div(other: Int): Fraction {
        return Fraction(numer to denom * other)
    }
    operator fun div(other: Fraction): Fraction {
        return Fraction(this.numer * other.denom to this.denom * other.numer)
    }

    override operator fun unaryMinus(): Fraction {
        return Fraction(-numer to denom)
    }

    operator fun inc(): Fraction {
        return this + 1
    }


    override fun toString(): String = "$numer/$denom"
    fun toFloat(): Float = numer.toFloat() / denom
}