package expressions.numerical

import expressions.Expression
import utils.LCD
import kotlin.math.abs

data class NumFraction(override val body: Pair<Int, Int>) : Expression() {
    val numer = body.first
    val denom = body.second

    override fun simplified(): NumFraction {
        return simplifiedSoftly()
    }
    override fun simplifiedSoftly(): NumFraction {
        val lcd = LCD(numer, denom)
        val newNumerator = (if (denom < 0) -numer else numer) / lcd
        val newDenominator = abs(denom) / lcd
        return NumFraction(newNumerator to newDenominator)
    }


    operator fun plus(other: Int): NumFraction {
        return NumFraction(numer + other * denom to denom)
    }
    operator fun plus(other: NumFraction): NumFraction {
        val newN = this.numer * other.denom + other.numer * this.denom
        val newD = this.denom * other.denom
        return NumFraction(newN to newD)
    }

    operator fun minus(other: Int): NumFraction {
        return NumFraction(numer - other * denom to denom)
    }
    operator fun minus(other: NumFraction): NumFraction {
        val newN = this.numer * other.denom - other.numer * this.denom
        val newD = this.denom * other.denom
        return NumFraction(newN to newD)
    }

    operator fun times(other: Int): NumFraction {
        return NumFraction(numer * other to denom)
    }
    operator fun times(other: NumFraction): NumFraction {
        return NumFraction(this.numer * other.numer to this.denom * other.denom)
    }

    operator fun div(other: Int): NumFraction {
        return NumFraction(numer to denom * other)
    }
    operator fun div(other: NumFraction): NumFraction {
        return NumFraction(this.numer * other.denom to this.denom * other.numer)
    }

    override operator fun unaryMinus(): NumFraction {
        return NumFraction(-numer to denom)
    }

    operator fun inc(): NumFraction {
        return this + 1
    }
}