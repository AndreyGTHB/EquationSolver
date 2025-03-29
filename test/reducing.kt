import expressions.numerical.Fraction
import utils.toMonomial

fun main() {
    // Fraction
    val f1 = Fraction(6 to 13).simplify()
    val f2 = Fraction(2 to 3).simplify()
    val m1 = "2*a*b*c*c".toMonomial().simplify()
    println(f1.reduceOrNull(f2))
    println(f1.reduceOrNull(f1))
    println(f1.reduceOrNull(m1))

    // Monomial
    val m2 = "a*b".toMonomial().simplify()
    val m3 = "b*c*e".toMonomial().simplify()
    val m4 = "a*a*c".toMonomial().simplify()
    println(m1.reduceOrNull(f1))
    println(m1.reduceOrNull(m2))
    println(m1.reduceOrNull(m3))
    println(m1.reduceOrNull(m4))
}