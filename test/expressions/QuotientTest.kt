package expressions

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import parser.parse

class QuotientTest {
    @Test
    fun `Irrationality in the denominator`() {
        val q1 = "1 / 5^(2/3)".parse()
        val q2 = "5^(1/3) / 5".parse()
        assertEquals(q2.simplify(), q1.simplify())
    }
}