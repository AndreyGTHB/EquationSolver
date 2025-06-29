import expressions.number.over
import expressions.number.power
import expressions.unit
import utils.power

fun main() {
    val a = 2.power(1 over 2)
    val b = 3.power(2 over 2)
    val c = 25.power(-3 over 2)
    println(c.simplify())

    val diffOfSquares = (b-a) * (a+b)
    println(diffOfSquares.simplify())

    val d1 = (1 over 2) * 2.power(1 over 2)
    val d2 = unit() / 2.power(1 over 2)
    println((d1 + d2).simplify())
}