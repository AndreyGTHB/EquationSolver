package utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.currentCoroutineContext
import kotlin.coroutines.CoroutineContext

suspend fun <T> coroutineScope(context: CoroutineContext, block: suspend CoroutineScope.() -> T): T {
    val scope = CoroutineScope(context)
    return scope.async { block(this) } .await()
}
suspend fun <T> CoroutineScope.coroutineScopeInheriting(block: suspend CoroutineScope.() -> T): T {
    return coroutineScope(currentCoroutineContext(), block)
}
