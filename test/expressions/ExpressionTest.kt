package expressions

import org.junit.jupiter.api.Test
import parser.parseExpression

class ExpressionTest {
    @Test
    fun simplify() {
        val e1 = "(-5(a + b) + 5b) / ((b + c)^(1/6) * (c + b)^(1/6))".parseExpression()
        e1.simplify()
        println(e1.unparse())
    }

    @Test
    fun domain() {
        val e1 = "(x + 1) / ((x + 1)(x - 1))".parseExpression()
        e1.simplify().domain.printlnColoured()
    }
}