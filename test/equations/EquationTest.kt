package equations

import expressions.buildExpressionFromUnit
import expressions.buildExpressionFromZero
import expressions.monomials.Monomial
import expressions.number.over
import expressions.number.power
import expressions.number.squareRoot
import expressions.number.toRational
import expressions.unit
import expressions.zero
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import utils.toMonomial

class EquationTest {
    val xMon = Monomial('x' to unit()).simplify()

    @Test
    fun `Empty Equations`() {
        val eq1 = zero() equateTo zero()
        val eq2 = 10.power(3 over 2) equateTo (2.power(3 over 2) * 5.power(15 over 10))
        assertEquals(eq1.solve(), FullDomain)
        assertEquals(eq2.solve(), FullDomain)

        val eq3 = unit() equateTo zero()
        val eq4 = (5.squareRoot() * xMon + 7.toRational()) equateTo (10.power(3 over 2) / (2.power(3 over 2) * 5.toRational()) * xMon)
        // eq4: 5x + 7 = (10^(3/2) / (2^(3/2) * 5))x
        assertEquals(eq3.solve(), EmptyDomain)
        assertEquals(eq4.solve(), EmptyDomain)
    }

    @Test
    fun `Simple equations`() {
        val eq1 = Monomial('x' to unit()) equateTo (5.power(2 over 3))
        eq1.solve().apply {
            assertTrue { this is Equation && left == eq1.left && right == eq1.right && final }
        }

        val eq2 = "x".toMonomial() * 10.toRational() equateTo "x".toMonomial() * (7 over 1) + (9 over 1)
        assertEquals(xMon equateTo 3.toRational(), eq2.solve())

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
        val eq3 = left3 equateTo right3
        println(eq3.solve())
    }
}

