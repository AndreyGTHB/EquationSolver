import expressions.monomials.Monomial
import expressions.number.over
import expressions.number.toRational
import expressions.zero
import utils.toMonomial

fun main() {
    val m1 = "5*a".toMonomial().simplify()
    val m2 = "a".toMonomial().simplify()
    println(m2.reduceOrNull(m1))

    val m3 = Monomial(mapOf('v' to zero())) * 3.toRational()
    println(m3.simplify())

    val m4 = "9/10*a*a*x*p".toMonomial()
    println(m4)

    val m5 = "a*b".toMonomial() * (2 over 3)
    println(m5)
    println(m5.simplify())
}