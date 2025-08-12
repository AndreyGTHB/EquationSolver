package expressions

import expressions.binary.Power
import expressions.number.calcSubScale
import expressions.number.over
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Test
import parser.parseExpression

class NumericalExprTest {
    val a = 5 over 3
    @Test
    fun approx() {
        assertEquals("2".toBigDecimal(), a.approx(0))
        assertEquals("0.16667".toBigDecimal(), "1/2 - 1/3".parseExpression().approx(5))
        assertEquals("6.00".toBigDecimal(), "(1/10)*(1/10)*100 / (1/2 - 1/3)".parseExpression().approx(2))

        val rootOfTwo = "(1/3 + 5/3)^(1/3 + 1/6)".parseExpression()
        assertEquals("1.41421356".toBigDecimal(), "(1/3 + 5/3)^(1/3 + 1/6)".parseExpression().approx(8))
        assertEquals("0.7071068".toBigDecimal(), Power(rootOfTwo to -one()).approx(7))
        assertEquals("0.7071068".toBigDecimal(), "(1/3 + 5/3)^((-1) * (1/3 + 1/6))".parseExpression().approx(7))
        assertEquals("8.8818E-16".toBigDecimal(), "(1/3 + 5/3)^((-100) * (1/3 + 1/6))".parseExpression().approx(20))

        println("(1/3 + 5/3)^((-100) * (1/3 + 1/6))".parseExpression().simplify())
    }

    @Test
    fun comparison() {
        assertFalse((100000 over 12312312) lessThan (99999 over 12312312))
    }

    @Test
    fun utils() {
        val eps1 = "0.5".toBigDecimal().scaleByPowerOfTen(-4)
        assertEquals(4, calcSubScale(eps1))
    }
}