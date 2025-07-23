package expressions

import expressions.number.over
import expressions.number.power
import expressions.number.toRational
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import parser.parseExpression
import utils.toMonomial

class ProductTest {
    val a = "a".parseExpression().simplify()

    @Test
    fun `Zero product`() {
        val p1 = "ab".toMonomial() * (((3 over 1) - ((8.power(1 over 2) - 2.power(1 over 2)) * ((8.power(1 over 2) + 2.power(1 over 2)) / 2.toRational()))))
        assert(p1.simplify() == zero())
    }

    @Test
    fun `Product with powers`() {
        val p1 = "a^(1/2) * a^(1/2) + 5/4".parseExpression()
        assertEquals((5 over 4) + a, p1.simplify())
    }

    @Test
    fun simplify() {
        val p1 = "(b + c)^(1/6) * (c + b)^(1/6)".parseExpression()
        assertEquals("(b + c)^(1/3)".parseExpression(), p1.simplify())
    }
}