import expressions.Expression
import expressions.number.over
import expressions.number.power
import expressions.one
import kotlinx.coroutines.*
import kotlin.time.measureTime

fun main() = runBlocking(Dispatchers.Default) {
    val timeNeeded = measureTime { coroutineScope {
//        repeat(100_000) { launch {
//            testJob()
//        }}
        repeat (100) { testJob() }
    }}
    println(testJob())
    println(testJob().domain)
    println("Time needed: $timeNeeded")
}

fun testJob(): Expression {
    val r2 = 2.power(1 over 2)
    val r3 = 3.power(1 over 2)
    val r5 = 5.power(1 over 2)
    val r23 = 2.power(1 over 3)
    val r73 = 7.power(1 over 3)
    val a = (r3-r2) * (r3+r2)
    val b = (r23-r73) * (r23*r23 + 14.power(1 over 3) + r73*r73)
    val c = (r5 - one())*(r5 - one()) + (2 over 1)*(r5 - one()) + one()*one()

    val expr = a + c/b
    return expr.simplify()
}
