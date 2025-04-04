import expressions.Expression
import expressions.longs.Sum
import expressions.numerical.Fraction
import utils.toMonomial

fun main() {
    // Fraction
    val f1 = Fraction(6 to 13)
    val f2 = Fraction(2 to 3)
    val m1 = "2*a*b*c*c".toMonomial().simplify()
//    println(f1.reduceOrNull(f2))
//    println(f1.reduceOrNull(f1))
//    println(f1.reduceOrNull(m1))

    // Monomial
    val m2 = "a*b".toMonomial().simplify()
    val m3 = "b*c*e".toMonomial().simplify()
    val m4 = "a*a*c".toMonomial().simplify()
//    println(m1.reduceOrNull(f1))
//    println(m1.reduceOrNull(m2))
//    println(m1.reduceOrNull(m3))
//    println(m1.reduceOrNull(m4))

    var m5: Expression = Fraction(8 to 6) * m3
    val m6 = Fraction(-3 to 4) * m1
//    println(m5)
    m5 = m5.simplify()
//    println(m5)
//    println(m6.simplify())
//    println(m4*m5)
//    println((m4*m5).simplify())
//    println((m5*m6).simplify())

    val m7 = (m4 * m5).simplify()
    val m8 = (m5 * m6 * "a".toMonomial()).simplify()
//    println(m7)
//    println(m8)
//    println(m8.reduceOrNull(m7))

    // Sums
    val s1 = Sum(listOf(f1, f2))
    val s3 = m1 + s1
//    println(s3); println(s3.simplify())
    val s4 = s3 - Fraction(23 to 39) - Fraction(21 to 39)
//    println(s4.simplify())
    val s5 = s4 + m8 + m7
//    println(s5); println(s5.simplify())
    val s6 = s5.simplify() as Sum
    val cif_s6 = s6.commonInternalFactor()
//    println(cif_s6)
//    println(s6.reduceOrNull(cif_s6))

    // Product
    val p1 = s4 * s5
//    println(p1)
//    println(p1.simplify())
//    println(p1.simplify(true))

    // Quotient
    val q1 = m1 / m2
//    println(q1); println(q1.simplify())
    val q2 = (m2 * f2) / m4
//    println(q2); println(q2.simplify())
    val q3 = (q2 *  s5) / m3
    println(q3); println(q3.simplify())
}