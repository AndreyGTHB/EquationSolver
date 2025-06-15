package expressions.longs

import expressions.Expression
import kotlinx.coroutines.*
import org.slf4j.LoggerFactory

abstract class LongExpression (override val body: List<Expression>, final: Boolean) : Expression(final) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun _isNumber() = body.all { it.isNumber() }

    protected suspend fun simplifyBody(): List<Expression> {
//        logger.info(".")
        val scope = CoroutineScope(Dispatchers.Default)
        val sBody = scope.async {
            body.map {
                async { it.simplify() }
            }.awaitAll()
        }.await()
        return sBody
    }

    override fun toString(): String {
        var asString = "${this::class.simpleName}:\n"
        body.forEach { subExp ->
            val subExpStrs = subExp.toString().split("\n")
            subExpStrs.forEach { subExpStr -> asString += "  $subExpStr\n" }
        }
        return asString
    }
}
