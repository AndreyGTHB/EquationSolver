package expressions.numerical

import expressions.Expression
import utils.LCD
import kotlin.math.abs

data class NumFraction(override val body: Pair<Int, Int>) : Expression() {
    val numerator: Int
        get() = body.first
    val denominator: Int
        get() = body.second

    override fun simplified(): NumFraction {
        return simplifiedSoftly()
    }
    override fun simplifiedSoftly(): NumFraction {
        val lcd = LCD(numerator, denominator)
        val newNumerator = (if (denominator < 0) -numerator else numerator) / lcd
        val newDenominator = abs(denominator) / lcd
        return NumFraction(newNumerator to newDenominator)
    }


    operator fun plus(other: Int): NumFraction {
        return NumFraction(numerator + other * denominator to denominator)
    }
    operator fun plus(other: NumFraction): NumFraction {
        val newN = this.numerator * other.denominator + other.numerator * this.denominator
        val newD = this.denominator * other.denominator
        return NumFraction(newN to newD)
    }

    operator fun minus(other: Int): NumFraction {
        return NumFraction(numerator - other * denominator to denominator)
    }
    operator fun minus(other: NumFraction): NumFraction {
        val newN = this.numerator * other.denominator - other.numerator * this.denominator
        val newD = this.denominator * other.denominator
        return NumFraction(newN to newD)
    }

    operator fun times(other: Int): NumFraction {
        return NumFraction(numerator * other to denominator)
    }
    operator fun times(other: NumFraction): NumFraction {
        return NumFraction(this.numerator * other.numerator to this.denominator * other.denominator)
    }

    operator fun div(other: Int): NumFraction {
        return NumFraction(numerator to denominator * other)
    }
    operator fun div(other: NumFraction): NumFraction {
        return NumFraction(this.numerator * other.denominator to this.denominator * other.numerator)
    }

    operator fun unaryMinus(): NumFraction {
        return NumFraction(-numerator to denominator)
    }

    operator fun inc(): NumFraction {
        return this + 1
    }
}