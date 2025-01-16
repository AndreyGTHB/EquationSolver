package expressions.binary

import expressions.Expression
import expressions.numerical.Integer
import utils.LCD

class NumFraction(override var body: Pair<Int, Int>) : Expression() {
    val numerator: Int
        get() = body.first
    val denominator: Int
        get() = body.second

    override fun simplify() {
        val lcd = LCD(numerator, denominator)
        body = Pair(numerator / lcd, denominator / lcd)
    }

    operator fun timesAssign(other: Int) {
        body = numerator * other to denominator
    }
}