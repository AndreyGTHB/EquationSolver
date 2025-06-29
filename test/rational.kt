import expressions.longs.Product
import expressions.number.over
import utils.toMonomial

fun main() {
    val a = 4 over -2
//    println(a.simplify())
//    println(Product(listOf(a, a)).simplify())

    val m = "4*x*y*u*u".toMonomial() as Product
    println((m * a).simplify())
}