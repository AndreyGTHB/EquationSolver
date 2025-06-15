import kotlinx.coroutines.*
import org.slf4j.LoggerFactory

val logger = LoggerFactory.getLogger("coroutineGames")

fun main() = runBlocking<Unit> {
    val scope = CoroutineScope(Dispatchers.Default)
    coroutineScope {  }
}