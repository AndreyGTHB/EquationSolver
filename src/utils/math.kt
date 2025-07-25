package utils

import kotlin.math.abs

fun gcd(a: Int, b: Int): Int {
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

fun Collection<Int>.gcd(): Int = fold(0, ::gcd)

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

fun Int.power(exponent: Int): Int {
    val exponentsOfTwo = mutableListOf<Int>()
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
    var currMultiple = this
    for (exp in exponentsOfTwo) {
        repeat(exp) { currMultiple *= currMultiple }
        result *= currMultiple
        currMultiple = this
    }
    return result
}
