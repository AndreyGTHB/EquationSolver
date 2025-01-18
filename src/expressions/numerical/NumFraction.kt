package expressions.numerical

import expressions.Expression
import utils.LCD
import kotlin.math.abs

data class NumFraction(private var _body: Pair<Int, Int>) : Expression() {
    override val body
        get() = _body
    val numerator: Int
        get() = _body.first
    val denominator: Int
        get() = _body.second

    override fun simplified(): NumFraction {
        val lcd = LCD(numerator, denominator)
        val new = NumFraction(_body)
        new.simplifyBody()
        return new
    }
    override fun simplifyBody() {
        val isNegative = (numerator < 0) xor (denominator < 0)
        val lcd = abs(LCD(numerator, denominator))
        _body = numerator / lcd to denominator / lcd
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