import expressions.number.over
import expressions.number.power
import utils.power

fun main() {
    val a = 2.power(3 over 2)
    val b = 3.power(3 over 2)
    val c = 5.power(-3 over 2)
    println(c.simplify())

    val diffOfSquares = (a-b) * (a+b)
    println(diffOfSquares.simplify())
}