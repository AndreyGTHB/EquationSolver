package expressions

import expressions.Expression.Companion.commonFactor
import expressions.number.over
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ReducingTest {
    @Test
    fun `Rational cf`() {
        val five = (10 over 1).simplify()
        val twentyFive = (25 over 2).simplify()
        assertEquals((5 over 2), commonFactor(five, twentyFive))
    }
}