import expressions.number.toRational
import expressions.unit
import kotlinx.coroutines.runBlocking
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import parser.parse
import utils.toMonomial
import kotlin.time.measureTime

val logger: Logger = LoggerFactory.getLogger("power")

fun main() {
    val timeNeeded = measureTime { test(1) }
    logger.info("Time needed: $timeNeeded")
    logger.info("Time per single test: {}", timeNeeded / 1)
}

fun test(numberOfTests: Int) { repeat(numberOfTests) {
    val s1 = "a + b".parse()
    val exponents2 = arrayOf(unit(), 2.toRational(), 3.toRational(), 4.toRational())
    exponents2.forEach {
        val p = s1 raisedTo it
        println(p.simplify())
    }
}}