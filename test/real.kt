import expressions.Expression
import expressions.number.Real
import expressions.unit
import kotlinx.coroutines.runBlocking
import utils.over
import utils.power
import utils.toMonomial

fun main() = runBlocking {
    val a = 2.power(3 over 2)
    val b = 3.power(3 over 2)
    val c = 5.power(-3 over 2)
    println(c.simplify())

    val diffOfSquares = (a-b) * (a+b)
    println(diffOfSquares.simplify())
}