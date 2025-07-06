package expressions

import expressions.longs.Product
import expressions.longs.Sum
import expressions.number.over
import expressions.number.squareRoot
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import utils.toMonomial

class SumTest {
    @Test
    fun `Extracting numerical common factor`() {
        val s1 = (5 over 1)*"a*b".toMonomial() + (5 over 1)*"a".toMonomial() + (30 over 2)
        val pr1 = s1.simplify() as Product
        assertEquals((5 over 1), pr1.body[0])

        val s2 = s1 * 60.squareRoot()
        val pr2 = s2.simplify() as Product
        val numFactor2 = (5 over 1) * 60.squareRoot()
        assertEquals(numFactor2.simplify(), pr2.body[0])

    }

    @Test
    fun commonInternalFactorTest() {
        val s1 = (5 over 1)*"a*b".toMonomial() + (5 over 1)*"a".toMonomial() + (5 over 1)
        assertEquals(five(), (s1.simplify() as Sum).commonInternalFactor)
    }
}