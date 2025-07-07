package equations

import expressions.*
import expressions.monomials.Monomial
import expressions.number.over
import expressions.number.power
import expressions.number.toRational
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import parser.parseEquation
import parser.parseExpression
import statements.UniversalSet
import statements.equalsTo
import statements.notEqualsTo

class EquationTest {
    val xMon = Monomial('x' to unit()).simplify()

    @Test
    fun `Empty Equations`() {
        val eq1 = Equation(zero(), zero())
        val eq2 = Equation("10^(3/2)".parseExpression(), "2^(3/2) * 5^(15/10)".parseExpression())
        assertEquals(eq1.solve().answer.expr, UniversalExpression)
        assertEquals(eq2.solve().answer.expr, UniversalExpression)

        val eq3 = Equation(unit(), zero())
        val eq4 = Equation("5x + 7".parseExpression(), "10^(3/2) / (2^(3/2) * 5^(1/2)) * x".parseExpression())
        assertEquals(eq3.solve().answer.expr, InvalidExpression)
        assertEquals(eq4.solve().answer.expr, InvalidExpression)
    }

    @Test
    fun `Simple equations`() {
        val eq1 = Equation(xMon, 5.power(3 over 2))
        eq1.solve().apply {
            assertEquals(UniversalSet, domain)
            assertEquals('x' equalsTo 5.power(3 over 2).simplify(), answer)
        }

        val eq2 = Equation("10x".parseExpression(), "9 + 7x".parseExpression())
        assertEquals(3.toRational(), eq2.solve().answer.expr)
    }

    @Test
    fun `With parameters`() {
        val eq1 = Equation(xMon, "a/a".parseExpression())
        assertEquals(Solution('x' equalsTo unit(), 'a' notEqualsTo zero()), eq1.solve())

        val eq2 = Equation(xMon, "a / (b - 1)".parseExpression())
        eq2.solve().also { (answer, domain) ->
            assertEquals('x' equalsTo "a / ((-1) + b)".parseExpression(), answer)
            assertEquals('b' notEqualsTo unit(), domain)
        }

        val eq3 = "ax - a = 0".parseEquation()
        println(eq3.solve())
    }
}

