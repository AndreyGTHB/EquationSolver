package expressions.number

import ch.obermuhlner.math.big.BigDecimalMath.log10
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode

fun calcDelta(scale: Int): BigDecimal = "0.5".toBigDecimal().scaleByPowerOfTen(-scale)

fun calcSubScale(epsilon: BigDecimal) = -log10(BigDecimal.TWO * epsilon, MathContext(2))
    .setScale(0, RoundingMode.UP)
    .toInt()

