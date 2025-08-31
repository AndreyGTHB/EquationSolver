package expressions

import org.junit.jupiter.api.Test
import parser.parseExpression

class UnparsingTest {
    @Test
    fun unparsing() {
        val e1 = "a + b * (c^2)".parseExpression()
        println(e1.unparse())
    }
}