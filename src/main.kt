import expressions.longs.Sum
import expressions.unit
import utils.over
import utils.toMonomial

//import expressions.*

fun main() {
    val q1 = "q".toMonomial()
    val q2 = "Q".toMonomial()
    val q3 = "P".toMonomial()
    val f1 = "f".toMonomial()
    val f2 = "F".toMonomial()
    val f0 = "I".toMonomial()

    val a = -(q1 + q2)/(q1+q3)
    val df = f1 + a*f0 - f2
    val qq = q1 * (unit() + a)
    val c = qq / df
    println(c.simplify())
}
