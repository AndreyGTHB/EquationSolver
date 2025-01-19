import expressions.monomials.*
import expressions.numerical.NumFraction
import utils.toMonomial

//import expressions.*

fun main() {
    val mon1 = "10*a*b*c*c".toMonomial()
    var mon2 = "-10*b*c*a*c".toMonomial()
    mon2 /= 3
    println("$mon1 $mon2")

    var sum = mon1 + mon2
    println(sum)
    sum += mon2 * 2
    println("Softly: ${sum.simplifiedSoftly()}")
    println("Fully: ${sum.simplified()}")
}
