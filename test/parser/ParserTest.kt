package parser

import expressions.buildExpressionFromZero
import expressions.monomials.Monomial
import expressions.number.toRational
import expressions.three
import expressions.two
import expressions.unit
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import kotlin.reflect.KFunction
import kotlin.time.measureTime

class ParserTest {
    val a = Monomial('a' to unit()).simplify()
    val b = Monomial('b' to unit()).simplify()
    val ab = (a * b).simplify()
    val eleven = 11.toRational().simplify()

    @Test
    fun _parse() {
        assertEquals(eleven, "11"._parse())
        assertEquals(ab, "ab"._parse())
        assertEquals(eleven*ab, "11*ab"._parse())
    }

    @Test
    fun standardize() {
        assertEquals("11*abc", "11a*b*c".standardize())
        assertEquals("(x+y)*(x-yy)", "(x + y)(x - y*y)".standardize())
        assertEquals("17/1+5/4*((ab)*c)*x", "17/1 + 5/4 * ((a*b)c)x".standardize())
    }

    @Test
    fun parse() {
        assertEquals(a, "a".parse())
        assertEquals(eleven, "11".parse())
        assertEquals(ab, "ab".parse())
        assertEquals(eleven*ab, "11ab".parse())

        val squareOfSum = buildExpressionFromZero {
            base(a raisedTo two())
            plus(two() * ab)
            plus (b raisedTo two())
        }
        assertEquals(squareOfSum, "a^2 + 2ab + b^2".parse())
        assertEquals(squareOfSum, "(a^2) + 2a*b + b^((2))".parse())

        assertEquals("(x-y)(x-y)(x-y)".parse().simplify(), "(x-y)^3".parse().simplify())

        println("-a".parse())
    }

    @Test
    fun benchMark() {
        val str = "(((x*x + 2*x*y + y*y)^(a*b^(5/2) - 12))/(a^(-x^(-a*(-b)))))*(a + b/2)/(((x*x + 2*x*y + y*y)^(a*b^(5/2) - 12))/(a^(-x^(-a*(-b)))))*(a + b/2) + (((x*x + 2*x*y + y*y)^(a*b^(5/2) - 12))/(a^(-x^(-a*(-b)))))*(a + b/2) - (((x*x + 2*x*y + y*y)^(a*b^(5/2) - 12))/(a^(-x^(-a*(-b)))))*(a + b/2) * (((x*x + 2*x*y + y*y)^(a*b^(5/2) - 12))/(a^(-x^(-a*(-b)))))*(a + b/2) ^ (((x*x + 2*x*y + y*y)^(a*b^(5/2) - 12))/(a^(-x^(-a*(-b)))))*(a + b/2) / (((x*x + 2*x*y + y*y)^(a*b^(5/2) - 12))/(a^(-x^(-a*(-b)))))*(a + b/2) + (((x*x + 2*x*y + y*y)^(a*b^(5/2) - 12))/(a^(-x^(-a*(-b)))))*(a + b/2) * (((x*x + 2*x*y + y*y)^(a*b^(5/2) - 12))/(a^(-x^(-a*(-b)))))*(a + b/2) / (((x*x + 2*x*y + y*y)^(a*b^(5/2) - 12))/(a^(-x^(-a*(-b)))))*(a + b/2) - (((x*x + 2*x*y + y*y)^(a*b^(5/2) - 12))/(a^(-x^(-a*(-b)))))*(a + b/2) -(((x*x + 2*x*y + y*y)^(a*b^(5/2) - 12))/(a^(-x^(-a*(-b)))))*(a + b/2) + (((x*x + 2*x*y + y*y)^(a*b^(5/2) - 12))/(a^(-x^(-a*(-b)))))*(a + b/2)"
        val timeNeeded = measureTime {
            for (i in 0 ..< 10000) {
                val parsed = str.parse()
            }
        }
        println(timeNeeded)
    }
}