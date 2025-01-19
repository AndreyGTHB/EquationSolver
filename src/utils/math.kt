package utils

import kotlin.math.abs

fun LCD(a: Int, b: Int): Int {
    val big = if (a > b) a else b
    val small = if (a <= b) a else b
    if (small == 0) return big
    if (big == 0) return abs(small)
    return LCD(b, a % b)
}