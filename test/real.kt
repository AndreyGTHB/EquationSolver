import expressions.numerical.Real
import expressions.unit
import utils.over
import utils.power

fun main() {
    val a = Real(-2 to (4 over 1))
    println(a)
    println(a.simplify())
}