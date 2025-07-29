package expressions

import expressions.longs.Product
import expressions.longs.Sum
import expressions.number.over
import org.junit.jupiter.api.Test
import parser.parseExpression

class NumericalExprTest {
    val a = 5 over 3
    @Test
    fun approx() {
        println(a.approx(-1))
        println("1/2 - 1/3".parseExpression().approx(5))
        println(Product(1 over 10, 2 over 10, 200 over 1).approx(2))
    }
}