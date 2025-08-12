package rules

import expressions.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import parser.parseExpression
import rules.statements.equalsTo
import rules.statements.greaterThan
import rules.statements.lessThan

class VibeRuleTest1 {

    @Test
    fun inequalities_implications() {
        // LessThan: monotonic implication
        assertTrue('a'.lessThan(four()).finalOn() implies 'a'.lessThan(five()).finalOn())
        assertFalse('a'.lessThan(four()).finalOn() implies 'a'.lessThan(two()).finalOn())

        // GreaterThan: monotonic implication
        assertTrue('a'.greaterThan(one()).finalOn() implies 'a'.greaterThan(zero()).finalOn())
        assertFalse('a'.greaterThan(one()).finalOn() implies 'a'.greaterThan(two()).finalOn())
    }

    @Test
    fun inequalities_contradictions_with_equals() {
        val gt = 'x'.greaterThan(three()).finalOn()
        val lt = 'y'.lessThan(three()).finalOn()

        // GreaterThan vs EqualsTo
        assertTrue(gt contradicts ('x' equalsTo three()).finalOn()) // boundary
        assertTrue(gt contradicts ('x' equalsTo two()).finalOn())   // below bound
        assertFalse(gt contradicts ('x' equalsTo five()).finalOn()) // above bound

        // LessThan vs EqualsTo
        assertTrue(lt contradicts ('y' equalsTo three()).finalOn()) // boundary
        assertTrue(lt contradicts ('y' equalsTo five()).finalOn())  // above bound
        assertFalse(lt contradicts ('y' equalsTo two()).finalOn())  // below bound
    }

    @Test
    fun inequalities_simplify_expression_inside() {
        val lt = ('x' lessThan "1 + 1".parseExpression()).simplify()
        val gt = ('x' greaterThan "2 - 1".parseExpression()).simplify()
        assertEquals('x' lessThan two(), lt)
        assertEquals('x' greaterThan one(), gt)
    }

    @Test
    fun inequalities_invalid_expression_becomes_contradiction() {
        val invalidExpr = "5 / ((5^(1/2))^2 - 5)".parseExpression() // simplifies to division by zero
        assertEquals(Contradiction, ('x' lessThan invalidExpr).simplify())
        assertEquals(Contradiction, ('x' greaterThan invalidExpr).simplify())
    }

    @Test
    fun inequalities_non_numeric_threshold_no_contradiction_with_equals() {
        val nonNumeric = "b".parseExpression()
        assertFalse(('a' greaterThan nonNumeric).finalOn() contradicts ('a' equalsTo one()).finalOn())
        assertFalse(('a' lessThan nonNumeric).finalOn() contradicts ('a' equalsTo one()).finalOn())
    }
}
