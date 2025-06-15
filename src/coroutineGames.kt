import kotlinx.coroutines.*
import org.slf4j.LoggerFactory
import org.slf4j.Logger
import utils.coroutineScope
import kotlin.coroutines.CoroutineContext
import kotlin.random.Random
import kotlin.time.Duration.Companion.milliseconds

val logger: Logger = LoggerFactory.getLogger("coroutineGames")

@OptIn(ExperimentalStdlibApi::class)
fun main() = runBlocking<Unit> {
    coroutineScope {
        logger.info("{}", currentCoroutineContext()[CoroutineDispatcher])
    }
    val scopeResult = coroutineScope(Dispatchers.Default) {
        launch {
            delay(1500)
            logger.info("Launch is over")
        }
        coroutineScope {
            logger.info("{}", currentCoroutineContext()[CoroutineDispatcher])
            delay(1000)
            logger.info("Child scope is over")
        }
        1
    }
    logger.info("Scope is done: {}", scopeResult)
}

fun <T> processRandom(block: Int.() -> T): T {
    val randomInt = Random.nextInt(10)
    return block(randomInt)
}
