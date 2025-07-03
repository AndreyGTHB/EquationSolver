package expressions

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import parser.parse

class RealTest {
    @Test
    fun `Simplification unambiguity`() {
        val rl1 = "5^(2/3)".parse()
        val rl2 = "25^(1/3)".parse()
        assertEquals(rl1, rl2.simplify())

        val rl3 = "6 * 18^(2/5)".parse()
        val rl4 = "(2^7 * 3^9)^(1/5)".parse()
        assertEquals(rl3, rl4.simplify())
    }
}