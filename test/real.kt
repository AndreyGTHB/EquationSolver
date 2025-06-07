import expressions.Expression
import expressions.number.Real
import expressions.unit
import utils.over
import utils.power
import utils.toMonomial

fun main() {
    val a = 2.power(3 over 2)
    val b = 3.power(3 over 2)
    println((a * b).simplify())

    var diffOfSquares: Expression = (a - b) * (a + b)
//    diffOfSquares *= (b - a) * (b*b + b*a + a*a)
    println(diffOfSquares)
    println(diffOfSquares.simplify())
}