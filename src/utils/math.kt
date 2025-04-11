package utils

import expressions.numerical.Rational
import kotlin.math.abs

fun GCD(a: Int, b: Int): Int {
    var (big, small) = abs(a) to abs(b)
    var t = big
    if (big < small) {
        big = small
        small = t
    }

    while (small != 0) {
        t = big
        big = small
        small = t % small
    }
    return big
}


infix fun Int.over(other: Int) = Rational(this to other)
fun Int.toFraction() = this over 1
