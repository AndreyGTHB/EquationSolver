package expressions

import expressions.longs.Product
import expressions.number.over
import org.junit.jupiter.api.Test
import parser.parseExpression

class NumericalExprTest {
    val a = 5 over 3
    @Test
    fun approx() {
        println(a.approx(0))
        println("1/2 - 1/3".parseExpression().approx(5))
        println(Product(1 over 10, 2 over 10, 200 over 1, 1 over 4, 4 over 3).approx(2))
    }
}