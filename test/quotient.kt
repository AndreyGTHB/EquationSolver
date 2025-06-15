import expressions.unit
import kotlinx.coroutines.runBlocking
import utils.over
import utils.toMonomial

fun main() = runBlocking {
    val a = "a".toMonomial()
    val b = "b".toMonomial()
    val mon1 = "23*a*b*b*e".toMonomial()
    val mon2 = "-1*d*a*d*b*c".toMonomial()
    var quot = mon1 / mon2
    println(quot.simplify())
//    println("${quot.numer} ${quot.denom}")
//    quot = quot.simplifySoftly()
//    println(quot)

//    var sum = quot + "5*e*f*f".toMonomial() * (4 over 1).flip() + (unit() / "f*e".toMonomial())
//    sum -= (2 over 1) / "f*e".toMonomial()
//    sum -= ("a".toMonomial() + "b*e".toMonomial()) / "-1*d*c*d".toMonomial()
//    println(sum)
//    println(sum.simplify())

    val qq = ((a / b) / (b / a)).simplify()
    println(qq)
}
