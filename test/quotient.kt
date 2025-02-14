import expressions.monomials.Monomial
import utils.toMonomial

fun main() {
    val mon1 = "23*a*b*b*e".toMonomial()
    val mon2 = "-1*d*a*d*b*c".toMonomial()
    val quot = mon1 / mon2
    println("${quot.numer} ${quot.denom}")
    println(quot.simplified())
}
