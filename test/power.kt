import expressions.number.toRational
import expressions.one
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import parser.parseExpression
import kotlin.time.measureTime

val logger: Logger = LoggerFactory.getLogger("power")

fun main() {
    val n = 1_000
    val timeNeeded = measureTime { test(n) }
    logger.info("Time needed: $timeNeeded")
    logger.info("Time per single test: {}", timeNeeded / n)
}

fun test(numberOfTests: Int) { repeat(numberOfTests) {
    val s1 = "a + b".parseExpression()
    val exponents2 = arrayOf(one(), 2.toRational(), 3.toRational(), 4.toRational())
    exponents2.forEach {
        val p = s1 raisedTo it
        p.simplify()
    }
}}