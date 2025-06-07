import expressions.monomials.Monomial
import expressions.zero
import utils.over
import utils.toRational
import utils.toMonomial

fun main() {
    val m1 = "5*a".toMonomial().simplify()
    val m2 = "a".toMonomial().simplify()
    println(m2.reduceOrNull(m1))

    val m3 = Monomial(mapOf('v' to zero())) * (3 over 1)
    println(m3.simplify())

    val m4 = "9/10*a*a*x*p".toMonomial()
    println(m4)
}