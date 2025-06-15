import expressions.binary.Power
import expressions.unit
import expressions.zero
import kotlinx.coroutines.runBlocking
import utils.over
import utils.toMonomial
import utils.toRational

fun main() = runBlocking {
    val b1 = 1 over 2
    val exponents1 = arrayOf(zero(), unit(), 1 over 2, unit() * 3, -5 over 3)
    exponents1.forEach { println(Power(b1 to it).simplify()) }

    val s1 = "a".toMonomial() + "b".toMonomial()
    val exponents2 = arrayOf(unit(), 2.toRational(), 3.toRational(), 4.toRational())
    exponents2.forEach {
        val p = s1 raisedTo it
        println(p.simplify())
    }
}