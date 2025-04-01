import expressions.monomials.Monomial
import utils.toFraction
import utils.toMonomial

fun main() {
    val m1 = "5*a".toMonomial().simplify()
    val m2 = "a".toMonomial().simplify()
    println(m2.reduceOrNull(m1))

    val m3 = Monomial(3.toFraction() to mapOf('v' to 0))
    println(m3.simplify())
}