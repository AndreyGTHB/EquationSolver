import utils.over
import utils.toMonomial

//import expressions.*

fun main() {
    val n1 = 5 over 4
    val n2 = 10 over 8
    val m1 = "a*b*c".toMonomial()
    val s1 = m1 + n1 - n2

    val expMap = mapOf(n1 to 1, s1.simplify() to 3, m1 to 2)
    println(expMap)
    println(expMap[n1])
    println(expMap[n2])
    println(expMap[n2.simplify()])
    println(expMap[s1])
    println(expMap[s1.simplify()])

    val map1 = mapOf(1 to -1, 2 to -2)
    val map2 = mapOf(2 to -2, 1 to -1)
    println(map1 == map2)
}
