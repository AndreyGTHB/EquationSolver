package expressions.numerical

import expressions.Expression
import expressions.longs.Product
import utils.GCD
import utils.toFraction
import kotlin.math.abs

data class Fraction(override val body: Pair<Int, Int>) : Expression(), Reducible {
    val numer = body.first
    val denom = body.second
    override val final get() = GCD(numer, denom) == 1

    init {
        if (denom == 0) { throw RuntimeException("Zero division") }
    }

    override fun simplify(): Fraction {
        return simplifySoftly()
    }
    override fun simplifySoftly(): Fraction {
        val gcd = GCD(numer, denom)
        val newNumerator = (if (denom < 0) -numer else numer) / gcd
        val newDenominator = abs(denom) / gcd
        return Fraction(newNumerator to newDenominator)
    }

    override fun commonFactor(other: Expression): Fraction {
        if (other is Fraction) {
            if (other.isNull()) return this
        }
        return 1.toFraction()
    }
    override fun reduceBy(other: Expression): Fraction {
        other as Fraction
        return this / other
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