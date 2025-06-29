package expressions

import expressions.number.over
import expressions.number.power
import expressions.number.toRational
import org.junit.jupiter.api.Test
import utils.toMonomial

class ProductTest {
    @Test
    fun `Zero product`() {
        val p1 = "ab".toMonomial() * (((3 over 1) - ((8.power(1 over 2) - 2.power(1 over 2)) * ((8.power(1 over 2) + 2.power(1 over 2)) / 2.toRational()))))
        assert(p1.simplify() == zero())
    }
}