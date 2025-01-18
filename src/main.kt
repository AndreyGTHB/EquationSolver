import expressions.monomials.*
import expressions.numerical.NumFraction

//import expressions.*

fun main() {
    val fr1 = NumFraction(4 to 3)
    var fr2 = fr1 / 2
    ++fr2
    println("Fr1: $fr1      Fr2: $fr2")
    val s = -(fr1 + fr2)
    println("Opposite sum: $s")
    println("Simplified opposite sum: ${s.simplified()}")
}
