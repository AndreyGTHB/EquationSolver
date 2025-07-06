package equations

import expressions.*
import expressions.monomials.Monomial
import expressions.number.over
import expressions.number.power
import expressions.number.squareRoot
import expressions.number.toRational
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import parser.parse
import statements.UniversalSet
import statements.equalsTo
import utils.toMonomial

class EquationTest {
    val xMon = Monomial('x' to unit()).simplify()

    @Test
    fun `Empty Equations`() {
        val eq1 = Equation(zero(), zero())
        val eq2 = Equation("10^(3/2)".parse(), "2^(3/2) * 5^(15/10)".parse())
        assertEquals(eq1.solve().answer.expr, UniversalExpression)
        assertEquals(eq2.solve().answer.expr, UniversalExpression)

        val eq3 = Equation(unit(), zero())
        val eq4 = Equation("5x + 7".parse(), "10^(3/2) / (2^(3/2) * 5^(1/2)) * x".parse())
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

        val eq2 = Equation("10x".parse(), "9 + 7x".parse())
        assertEquals(3.toRational(), eq2.solve().answer.expr)

        val left3 = buildExpressionFromUnit {
            times(4.toRational() - 2.power(5 over 3))
            times(xMon)
        }
        val right3 = buildExpressionFromUnit {
            times {
                times(7.squareRoot() - unit())
                times(7.toRational() * 28.squareRoot() + 2.toRational())
                minus {
                    times(2.toRational())
                    times(2.power(1 over 3))
                    times(xMon)
                }
            }
            div {
                plus(2.power(-2 over 3))
            }
        }
//        val left3 = buildExpressionFromUnit { times(2.toRational()) }
//        val right3 = buildExpressionFromUnit {
//            times {
//                minus {
////                    times(2.toRational())
////                    times(2.power(1 over 3))
//                    times(xMon)
//                }
//            }
//            div {
//                plus(2.squareRoot())
//            }
//        }
        val eq3 = Equation(left3, right3)
        println(eq3.solve())
    }

    @Test
    fun `With parameters`() {
        val eq1 = Equation(xMon, "a/a".parse())
        println(eq1.solve())
    }
}

