package expressions

import expressions.Expression.Companion.commonFactor
import expressions.longs.Sum
import expressions.monomials.Monomial
import expressions.number.over
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import parser.parseExpression

class ReducingTest {
    val a = Monomial(mapOf('a' to unit()), true)

    @Test
    fun `Rational cf`() {
        val five = (10 over 1).simplify()
        val twentyFive = (25 over 2).simplify()
        assertEquals((5 over 2), commonFactor(five, twentyFive))
    }

    @Test
    fun `Products and sums`() {
        val sum1 = "a + b".parseExpression().simplify()
        val sum2 = "a + b".parseExpression().simplify()
        assertEquals(sum1, commonFactor(sum1, sum2))
        assertEquals(unit(), sum1.reduce(sum2))

        val sum3 = "(5^(1/2) - 1)a^2 + (5^(1/2) - 1)ab".parseExpression().simplify()
        assertEquals(sum1, commonFactor(sum1, sum3))
        assertEquals("(5^(1/2) - 1)a".parseExpression().simplify(), sum3.reduce(sum1))

        val sum4 = "(5^(1/2) - 1)a + (5^(1/2) - 1)x".parseExpression().simplify()
        println((sum4 as Sum).commonInternalFactor)
    }
}