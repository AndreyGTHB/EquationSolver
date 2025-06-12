import expressions.commonFactor
import expressions.longs.Sum
import expressions.monomials.Monomial
import expressions.unit
import utils.over
import utils.power
import utils.toMonomial

fun main() {
    val r2 = 2.power(1 over 2)
    val r3 = 3.power(1 over 2)
    val r5 = 5.power(1 over 2)
    val r23 = 2.power(1 over 3)
    val r73 = 7.power(1 over 3)
    val a = (r3-r2) * (r3+r2)
    val b = (r23-r73) * (r23*r23 + 14.power(1 over 3) + r73*r73)
    val c = (r5 - unit())*(r5 - unit()) + (2 over 1)*(r5 - unit()) + unit()*unit()

    val expr = a + c/b
    println(expr.simplify())
    println((3 over 2) == (6 over 4))
}
