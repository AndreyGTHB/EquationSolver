package console

import expressions.*
import org.junit.jupiter.api.Test
import parser.parseExpression
import rules.*
import rules.statements.equalsTo
import rules.statements.notEqualsTo

class ColourableTest {
    @Test
    fun colouring() {
        val expr = "-1 + a/(bc) + (x^2 - y^2)".parseExpression()
        expr.printlnColoured()
        InvalidExpression.printlnColoured()
        UniversalExpression.printlnColoured()

        val statement = Conjunction(Disjunction('a' equalsTo five(), 'b' notEqualsTo three()), 'c' equalsTo "1 + b".parseExpression())
        statement.printlnColoured()
        Contradiction.printlnColoured()
        Tautology.printlnColoured()
    }
}