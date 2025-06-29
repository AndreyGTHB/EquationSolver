package expressions

import expressions.number.over
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import utils.toMonomial

class ExpressionBuilderTest {
    val a = "a".toMonomial().simplify()
    val b = "b".toMonomial().simplify()

    @Test
    fun `Numerical expressions`() {
        val one = buildExpressionFromZero {
            base(five())
            minus {
                base(two())
                raiseTo(two())
            }
        }
        assertEquals(unit(), one.simplify())
    }

    @Test
    fun `Non-numerical expressions`() {
        val nil = buildExpressionFromZero {
            base(a + b)
            raiseTo(two())
            minus(b) {
                raiseTo(two())
            }
            div(a)
            plus {
                base(a + two() * b)
                raiseTo(1 over 2)
                times((a + two() * b) raisedTo (1 over 2))
                times(-unit())
            }
        }
        assertEquals(zero(), nil.simplify())
    }
}