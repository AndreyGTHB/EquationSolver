package expressions.longs

import expressions.Expression
import kotlinx.coroutines.*
import org.slf4j.LoggerFactory

abstract class LongExpression (override val body: List<Expression>, final: Boolean) : Expression(final) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun _isNumber() = body.all { it.isNumber() }

    protected suspend fun simplifyBody(): List<Expression> {
        logger.trace(">> LongExpression.simplifyBody()")
        val scope = CoroutineScope(Dispatchers.Default)
        val sBodyDeferred = scope.async {
            logger.trace("   >> LongExpression.simplifyBody().coroutineScope()")
            body.mapIndexed { i, expr ->
                async {
                    logger.trace("      >> $i __0_0__ IN  .simplify()")
                    expr.simplify().also {
                        logger.trace("      << $i __0_0__ OUT .simplify()")
                    }
                }
            }.awaitAll().also {
                logger.trace("   << LongExpression.simplifyBody().coroutineScope()")
            }
        }
        logger.trace("<< LongExpression.simplifyBody()")
        return sBodyDeferred.await()
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
