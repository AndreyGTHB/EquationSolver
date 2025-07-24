package rules

import expressions.number.toRational
import expressions.one
import expressions.three
import expressions.two
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import parser.parseExpression
import rules.statements.equalsTo
import rules.statements.notEqualsTo

class VibeRuleTest {

    @Test
    fun `simplification of expression-based rules`() {
        // Expression simplifies to a constant
        val r1 = 'a' equalsTo "2+3".parseExpression()
        assertEquals('a' equalsTo 5.toRational(), r1.simplify())

        // Expression with a variable
        val r2 = 'x' equalsTo "b + b".parseExpression()
        assertEquals('x' equalsTo "2b".parseExpression(), r2.simplify())

//         Expression that is always true
//        val r3 = "x+y".parseExpression() equalsTo "y+x".parseExpression()
//        assertEquals(Tautology, r3.simplify())

        // Expression that leads to Contradiction (division by zero)
        val r4 = 'a' equalsTo "1 / (x - x)".parseExpression()
        assertEquals(Contradiction, r4.simplify())
    }

    @Test
    fun `simplification of Conjunction`() {
        val p = 'a' equalsTo one()
        val q = 'b' equalsTo two()
        val notp = 'a' notEqualsTo one()

        assertEquals(Contradiction, (p * notp).simplify(), "P and !P should be Contradiction")
        assertEquals(p.simplify(), (p * p).simplify(), "P and P should be P")
        assertEquals(Contradiction, (p * Contradiction).simplify(), "P and Contradiction should be Contradiction")
        assertEquals(p.simplify(), (p * Tautology).simplify(), "P and Tautology should be P")

        val r = 'c' equalsTo "2*3".parseExpression()
        val simplifiedR = 'c' equalsTo 6.toRational()
        assertEquals((p * simplifiedR).simplify(), (p * r).simplify(), "Nested rules should be simplified")
    }

    @Test
    fun `simplification of Disjunction`() {
        val p = 'a' equalsTo one()
        val q = 'b' equalsTo two()
        val notp = 'a' notEqualsTo one()

        assertEquals(Tautology, (p + notp).simplify(), "P or !P should be Tautology")
        assertEquals(p.simplify(), (p + p).simplify(), "P or P should be P")
        assertEquals(Tautology, (p + Tautology).simplify(), "P or Tautology should be Tautology")
        assertEquals(p.simplify(), (p + Contradiction).simplify(), "P or Contradiction should be P")

        val r = 'c' equalsTo "10-4".parseExpression()
        val simplifiedR = 'c' equalsTo 6.toRational()
        assertEquals((p + simplifiedR).simplify(), (p + r).simplify(), "Nested rules should be simplified")
    }

    @Test
    fun `simplification of Complement`() {
        val p = 'a' equalsTo "x*x".parseExpression()

        assertEquals(p.simplify(), (-(-p)).simplify(), "Double negation should be removed")

        val notp = 'a' notEqualsTo "x*x".parseExpression()
        assertEquals(notp.simplify(), (-p).simplify(), "Negation should be applied")

        assertEquals(Contradiction, (-Tautology).simplify(), "Negation of Tautology is Contradiction")
        assertEquals(Tautology, (-Contradiction).simplify(), "Negation of Contradiction is Tautology")
    }

    @Test
    fun `De Morgan's laws and complex simplifications`() {
        val p = 'a' equalsTo one()
        val q = 'b' equalsTo "5-3".parseExpression()

        val r1 = p * (q + (-q))
        assertEquals(p.simplify(), r1.simplify(), "P and (Q or !Q) should simplify to P")

        val r2 = p + (q * (-q))
        assertEquals(p.simplify(), r2.simplify(), "P or (Q and !Q) should simplify to P")

        // De Morgan's Law: -(A and B) <=> (!A or !B)
        val r3 = -(p * q)
        val expected1 = (-p) + (-q)
        assertEquals(expected1.simplify(), r3.simplify())

        // De Morgan's Law: -(A or B) <=> (!A and !B)
        val r4 = -(p + q)
        val expected2 = (-p) * (-q)
        assertEquals(expected2.simplify(), r4.simplify())
    }

    @Test
    fun `simplification is idempotent`() {
        val r = ('a' equalsTo "2+2".parseExpression()) * ('b' equalsTo three())
        val simplifiedOnce = r.simplify()
        val simplifiedTwice = simplifiedOnce.simplify()
        assertEquals(simplifiedOnce, simplifiedTwice, "Simplification result should be equal on subsequent calls")
        assertSame(simplifiedOnce, simplifiedTwice, "Simplification should return the same instance if already simplified")
    }

    @Test
    fun `nested structures simplification`() {
        val p = 'a' equalsTo one()
        val q = 'b' equalsTo two()
        val r = 'c' equalsTo three()

        // Absorption Law: A | (A & B) = A
        assertEquals(p.simplify(), (p + (p * q)).simplify(), "Disjunction with nested conjunction should simplify by absorption")
        assertEquals(p.simplify(), ((p * q) + p).simplify(), "Order should not matter for absorption")

        // Absorption Law: A & (A | B) = A
        assertEquals(p.simplify(), (p * (p + q)).simplify(), "Conjunction with nested disjunction should simplify by absorption")
        assertEquals(p.simplify(), ((p + q) * p).simplify(), "Order should not matter for absorption")

        // More complex absorptions
        val pAndQ = p * q
        val pAndQAndR = p * q * r
        assertEquals(pAndQ.simplify(), (pAndQ + pAndQAndR).simplify(), "A or (A and B) -> A where A is a conjunction")

        val pOrQ = p + q
        val pOrQOrR = p + q + r
        assertEquals(pOrQ.simplify(), (pOrQ * pOrQOrR).simplify(), "A and (A or B) -> A where A is a disjunction")

        // Nested test: (P & (Q | R)) | P simplifies to P
        val r1 = (p * (q + r)) + p
        assertEquals(p.simplify(), r1.simplify())

        // Nested test: (P | (Q & R)) & P simplifies to P
        val r2 = (p + (q * r)) * p
        assertEquals(p.simplify(), r2.simplify())

        // Distributive law is not expected to work, so test for that
        // (p & q) | (p & r) = p & (q | r) -> this is not implemented
        val r3 = (p * q) + (p * r)
        val expected3 = p * (q + r)
        // Simplification should not perform distribution
        assertNotEquals(expected3.simplify(), r3.simplify(), "Distributive law should not be applied")

        // Check simplification of inner expressions
        val pComplex = 'a' equalsTo "1+0".parseExpression()
        val r4 = (pComplex * q) + p
        assertEquals(p.simplify(), r4.simplify(), "Inner expressions should be simplified before absorption")
    }

}
