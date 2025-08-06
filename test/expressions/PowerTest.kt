package expressions

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import parser.parseExpression

class PowerTest {
    @Test
    fun simplify() {
        val p1 = "(1 - 1)^(1/2)".parseExpression()
        assertEquals(zero(), p1.simplify())
    }
}