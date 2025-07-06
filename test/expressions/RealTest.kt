package expressions

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import parser.parseExpression

class RealTest {
    @Test
    fun `Simplification unambiguity`() {
        val rl1 = "5^(2/3)".parseExpression()
        val rl2 = "25^(1/3)".parseExpression()
        assertEquals(rl1, rl2.simplify())

        val rl3 = "6 * 18^(2/5)".parseExpression()
        val rl4 = "(2^7 * 3^9)^(1/5)".parseExpression()
        assertEquals(rl3, rl4.simplify())
    }
}