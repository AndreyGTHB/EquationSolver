package rules

import expressions.number.squareRoot
import expressions.one
import expressions.three
import expressions.two
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
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

    @Test
    fun contradiction() {
        val r1 = ('a' equalsTo two()).simplify()
        val r2 = ('a' notEqualsTo two()).simplify()
        val r3 = ('b' notEqualsTo two()).simplify()
        val r4 = ('a' notEqualsTo three()).simplify()
        assertTrue(r1 contradicts r2)
        assertFalse(r1 contradicts r3)
        assertFalse(r1 contradicts r4)

        val r5 = ('x' equalsTo "-5a / (b + c)^(1/3)".parseExpression()).simplify()
        val r6 = ('x' notEqualsTo "(-5(a + b) + 5b) / ((b + c)^(1/6) * (c + b)^(1/6))".parseExpression()).simplify()
        println(r6)
        assertTrue(r5 contradicts r6)
    }
}