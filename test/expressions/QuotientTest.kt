package expressions

import expressions.longs.Sum
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import parser.parseExpression

class QuotientTest {
    @Test
    fun `Irrationality in the denominator`() {
        val q1 = "1 / 5^(2/3)".parseExpression()
        val q2 = "5^(1/3) / 5".parseExpression()
        assertEquals(q2.simplify(), q1.simplify())
    }

    @Test
    fun `Reducing by sums`() {
        val e1 = "(1 / (b - 1)) * (b - 1)".parseExpression()
        assertEquals(unit(), e1.simplify())

        val e2 = "(-(a) / (b - 1)) * (1 - b)".parseExpression()
        assertEquals("a".parseExpression(), e2.simplify())

        val e3 = "(b - c) / ((7^(1/3) - 2) * (c - b))".parseExpression()
        assertEquals("(-1) / (-2 + 7^(1/3))".parseExpression(), e3.simplify())
    }
}