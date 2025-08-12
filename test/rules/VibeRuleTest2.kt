package rules

import expressions.five
import expressions.four
import expressions.zero
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import parser.parseExpression
import rules.statements.equalsTo
import rules.statements.greaterThan
import rules.statements.lessThan

class VibeRuleTest2 {

    @Test
    fun `less than contradictions`() {
        val lessThan5 = 'x'.lessThan(five()).simplify()

        // Contradicts
        assertTrue(lessThan5 contradicts 'x'.greaterThan(five()).simplify())
        assertTrue(lessThan5 contradicts 'x'.greaterThan("6".parseExpression()).simplify())
        assertTrue(lessThan5 contradicts 'x'.equalsTo(five()).simplify())
        assertTrue(lessThan5 contradicts 'x'.equalsTo("6".parseExpression()).simplify())

        // Does not contradict
        assertFalse(lessThan5 contradicts 'x'.lessThan("6".parseExpression()).simplify())
        assertFalse(lessThan5 contradicts 'x'.greaterThan(four()).simplify())
        assertFalse(lessThan5 contradicts 'x'.equalsTo(four()).simplify())
        assertFalse(lessThan5 contradicts 'x'.equalsTo(zero()).simplify())
    }

    @Test
    fun `greater than contradictions`() {
        val greaterThan5 = 'x'.greaterThan(five()).simplify()

        // Contradicts
        assertTrue(greaterThan5 contradicts 'x'.lessThan(five()).simplify())
        assertTrue(greaterThan5 contradicts 'x'.lessThan(four()).simplify())
        assertTrue(greaterThan5 contradicts 'x'.equalsTo(five()).simplify())
        assertTrue(greaterThan5 contradicts 'x'.equalsTo(four()).simplify())

        // Does not contradict
        assertFalse(greaterThan5 contradicts 'x'.greaterThan(four()).simplify())
        assertFalse(greaterThan5 contradicts 'x'.lessThan("6".parseExpression()).simplify())
        assertFalse(greaterThan5 contradicts 'x'.equalsTo("6".parseExpression()).simplify())
    }

    @Test
    fun `less than implications`() {
        assertTrue('x'.lessThan(five()).simplify() implies 'x'.lessThan("6".parseExpression()).simplify())
        assertFalse('x'.lessThan(five()).simplify() implies 'x'.lessThan(four()).simplify())
        assertTrue('x'.lessThan(five()).simplify() implies 'x'.lessThan(five()).simplify())
    }

    @Test
    fun `greater than implications`() {
        assertTrue('x'.greaterThan(five()).simplify() implies 'x'.greaterThan(four()).simplify())
        assertFalse('x'.greaterThan(five()).simplify() implies 'x'.greaterThan("6".parseExpression()).simplify())
        assertTrue('x'.greaterThan(five()).simplify() implies 'x'.greaterThan(five()).simplify())
    }
}
