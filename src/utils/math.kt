package utils

import expressions.number.Rational
import kotlin.math.abs


fun gcd(a: Int, b: Int): Int {
    if (a * b == 0) TODO("Two zeros")
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
fun Collection<Int>.gcd(): Int {
    var cd = 0
    this.forEach { if (it != 0) cd = gcd(it, cd) }
    if (cd == 0) TODO("Zero collection")
    return cd
}

fun Int.factorise(): Map<Int, Int> {
    val decomp = mutableMapOf<Int, Int>()
    var remainder = this
    var i = 2
    while (i * i <= remainder) {
        while (remainder % i == 0) {
            decomp[i] = (decomp[i] ?: 0) + 1
            remainder /= i
        }
        i++
    }
    if (remainder != 1) decomp[remainder] = 1
    return decomp
}

fun power(base: Int, exponent: Int): Int {
    val exponentsOfTwo = mutableListOf<Int>()
    var n = 0
    var currPowerOfTwo = 1
    var currExponentOfTwo = 0
    var remainingExponent = exponent
    while (currPowerOfTwo * 2 <= exponent) {
        currPowerOfTwo *= 2
        currExponentOfTwo++
    }
    while (remainingExponent > 0) {
        if (remainingExponent >= currPowerOfTwo) {
            exponentsOfTwo.add(currExponentOfTwo)
            remainingExponent -= currPowerOfTwo
        }
        currPowerOfTwo /= 2
        currExponentOfTwo -= 1
    }

    var result = 1
    var currMultiple = base
    for (exp in exponentsOfTwo) {
        for (i in 1..exp) {
            currMultiple *= currMultiple
        }
        result *= currMultiple
        currMultiple = base
    }
    return result
}


infix fun Int.over(other: Int) = Rational(this to other)
fun Int.toRational() = this over 1

fun min(a: Rational, b: Rational): Rational = if ((a - b).isNegative()) a else b
