package expressions

import expressions.number.over
import expressions.number.power
import expressions.number.toRational
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import parser.parse
import utils.toMonomial

class ProductTest {
    val a = "a".parse().simplify()

    @Test
    fun `Zero product`() {
        val p1 = "ab".toMonomial() * (((3 over 1) - ((8.power(1 over 2) - 2.power(1 over 2)) * ((8.power(1 over 2) + 2.power(1 over 2)) / 2.toRational()))))
        assert(p1.simplify() == zero())
    }

    @Test
    fun `Product with powers`() {
        val p1 = "a^(1/2) * a^(1/2) + 5/4".parse()
        assertEquals((5 over 4) + a, p1.simplify())
    }
}