package expressions

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
}