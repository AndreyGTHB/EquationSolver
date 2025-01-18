import expressions.monomials.*
import expressions.numerical.NumFraction

//import expressions.*

fun main() {
    val m1 = "9*a*a*b".toMonomial()
    val m2 = "4*b*c*c".toMonomial()
    val m3 = m1 * m2
    println(m3)
}
