package utils

import expressions.numerical.Fraction
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


fun Int.toFraction(): Fraction {
    return Fraction(this to 1)
}
