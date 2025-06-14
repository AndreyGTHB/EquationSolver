import kotlinx.coroutines.*
import org.slf4j.LoggerFactory

val logger = LoggerFactory.getLogger("coroutineGames")

fun main() = runBlocking {
    val a = 5
    logger.info("bams")

    repeat(a) { i ->
        launch {
            logger.info("$i")
        }
    }
}