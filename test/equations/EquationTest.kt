package equations

import expressions.monomials.Monomial
import expressions.number.over
import expressions.number.power
import expressions.one
import expressions.three
import expressions.zero
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import parser.parseEquation
import parser.parseExpression
import rules.Contradiction
import rules.Tautology
import rules.statements.equalsTo
import rules.statements.notEqualsTo

class EquationTest {
    val xMon = Monomial('x' to one()).simplify()

    @Test
    fun `Empty Equations`() {
        val eq1 = Equation(zero() to zero())
        val eq2 = Equation("10^(3/2)".parseExpression() to "2^(3/2) * 5^(15/10)".parseExpression())
        assertEquals(Tautology, eq1.solve())
        assertEquals(Tautology, eq2.solve())

        val eq3 = Equation(one() to zero())
        val eq4 = "5x + 7 = 10^(3/2) / (2^(3/2) * 5^(1/2)) * x".parseEquation()
        assertEquals(Contradiction, eq3.solve())
        assertEquals(Contradiction, eq4.solve())

        val eq5 = "(x + 1) / ((x + 1)(x - 1)) = 0".parseEquation()
        eq5.solve().printlnColoured()
    }

    @Test
    fun `Simple equations`() {
        val eq1 = Equation(xMon to 5.power(3 over 2))
        assertEquals('x' equalsTo 5.power(3 over 2).simplify(), eq1.solve())

        val eq2 = Equation("10x".parseExpression() to "9 + 7x".parseExpression())
        assertEquals('x' equalsTo three(), eq2.solve())
    }

    @Test
    fun `With parameters`() {
        val eq1 = Equation(xMon to "a/a".parseExpression())
        val sol1 = ('x' equalsTo one()) * ('a' notEqualsTo zero())
        assertEquals(sol1, eq1.solve())

        val eq2 = Equation(xMon to "a / (b - 1)".parseExpression())
        val sol2 = ('x' equalsTo "a / ((-1) + b)".parseExpression()) * ('b' notEqualsTo one())
        assertEquals(sol2.simplify(), eq2.solve())


        val eq3 = "ax - a = 0".parseEquation()
        println("eq3: ${eq3.solve().coloured()}")

        val eq5 = "(7^(1/2) - 1)ax + (b/a)x = ax - b/5".parseEquation('x')
//        eq5.solve().printlnColoured()
    }

    @Test
    fun `Quadratic equations`() {
        val eq1 = "x^2 -2x + 1 = 0".parseEquation()
        val eq2 = "x = 1 + 1/x".parseEquation()
//        eq2.solve().printlnColoured()
        val eq3 = "(a)x^2 + bx c = 0".parseEquation('x')
//        eq3.solve().printlnColoured()
    }
}

