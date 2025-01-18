package utils

fun LCD(a: Int, b: Int): Int {
    val big = if (a > b) a else b
    val small = if (a <= b) a else b
    if (big == 0 || small == 0) return big
    return LCD(b, a % b)
}