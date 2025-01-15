import expressions.*
import expressions.binary.*

fun main() {
    val n1 = NumberExp(1)
    val n2 = NumberExp(2)
    var a: Expression = Sum(n1 to n2)
    println(a)
    print(a.simplify())
}
