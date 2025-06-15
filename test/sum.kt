import expressions.longs.Sum
import kotlinx.coroutines.runBlocking
import utils.over
import utils.toMonomial

fun main() = runBlocking {
    var s1 = Sum(listOf(1 over 2))
    s1 += 2 over 3
    s1 += 3 over 4

    val s2 = s1 + "7*y*x".toMonomial()
    print("7*y*x".toMonomial().simplify())
    print(s2)
    print(s2.simplify())
}