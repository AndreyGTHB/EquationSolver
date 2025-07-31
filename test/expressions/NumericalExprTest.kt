package expressions

import expressions.number.calcSubScale
import expressions.number.over
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import parser.parseExpression

class NumericalExprTest {
    val a = 5 over 3
    @Test
    fun approx() {
        println(a.approx(0))
        println("1/2 - 1/3".parseExpression().approx(5))
        println("(1/10)*(1/10)*100 / (1/2 - 1/3)".parseExpression().approx(2))
    }

    @Test
    fun utils() {
        val eps1 = "0.5".toBigDecimal().scaleByPowerOfTen(-4)
        assertEquals(4, calcSubScale(eps1))
    }
}