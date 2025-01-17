import expressions.monomials.*
import expressions.numerical.NumFraction

//import expressions.*

fun main() {
    val fr1 = NumFraction(3 to 1)
    println(fr1)
    val fr2 = fr1 / 2
    println(fr2)
    println(fr1 + fr2)
}
