package utils

import java.math.BigDecimal
import java.math.BigInteger

fun BigDecimal.rescale(): BigDecimal { // NOPT: O(n)
    val sign = signum()
    val unscaled = unscaledValue().abs().toString()
    var zeros = 0
    for (digit in unscaled.reversed()) {
        if (digit != '0') break
        zeros++
    }
    val lastDigitI = unscaled.lastIndex - zeros

    val newUnscaled = unscaled.slice(0 .. lastDigitI).toBigInteger()
    val newScale = scale() - zeros
    return BigDecimal(newUnscaled, newScale).let { if (sign == -1) it.negate() else it }
}

fun BigInteger.numberOfDigits() = toString().length
