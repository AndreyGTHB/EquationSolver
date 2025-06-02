import expressions.number.Real
import utils.over

fun main() {
    val a = Real(10 to (5 over 2))
    println(a)
    println(a.simplify())
}