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

    var currMon = mon1
    for (i in 0..10000) {
        currMon *= "a".toMonomial() * NumFraction(1 to 2)
        sum += currMon
    }
    sum = sum.simplifiedSoftly()
    println(1)
}
