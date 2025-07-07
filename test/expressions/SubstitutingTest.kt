package expressions

import expressions.number.toRational
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import parser.parseExpression

class SubstitutingTest {
    @Test
    fun substitute() {
        val e1 = "(a + 1)^4".parseExpression()
        assertEquals(81.toRational(), e1.substitute('a', two()).simplify())
        assertEquals(16.toRational(), e1.simplify().substitute('a', unit()).simplify())
    }
}