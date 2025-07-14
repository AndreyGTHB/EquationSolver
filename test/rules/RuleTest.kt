package rules

import expressions.number.squareRoot
import expressions.one
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import parser.parseExpression
import rules.statements.equalsTo
import rules.statements.notEqualsTo

class RuleTest {
    @Test
    fun complement() {
        val r1 = 'a' equalsTo one()
        val r2 = r1 * ('b' notEqualsTo 5.squareRoot())
        assertEquals('a' notEqualsTo one(), -r1)
        assertEquals(Disjunction(-r1, 'b' equalsTo 5.squareRoot()), -r2)
        assertEquals(r2, -(-r2))
    }

    @Test
    fun simplification() {
        val r1 = 'a' equalsTo "5 / ((5^(1/2))^2 - 5)".parseExpression()
        assertEquals(Contradiction, r1.simplify())
    }
}