package expressions.binary

import expressions.Expression
import expressions.number.Rational
import kotlinx.coroutines.*
import utils.over
import org.slf4j.LoggerFactory
import utils.coroutineScope

abstract class BinaryExpression (
    override val body: Pair<Expression, Expression>,
    final: Boolean
) : Expression(final) {
    private val logger = LoggerFactory.getLogger(BinaryExpression::class.java)

    override fun _isNumber() = body.first.isNumber() && body.second.isNumber()

    protected suspend fun simplifyBody(): Pair<Expression, Expression> {
        logger.debug(".")
        val sBody = coroutineScope(Dispatchers.Default) {
            val sFirstDeferred = async { body.first.simplify() }
            val sSecondDeferred = async { body.second.simplify() }
            sFirstDeferred.await() to sSecondDeferred.await()
        }
        return sBody
    }

    override fun toString(): String {
        var asString = "${this::class.simpleName}:\n"
        arrayOf(body.first, body.second).forEach { subExp ->
            val subExpStrs = subExp.toString().split("\n")
            subExpStrs.forEach { subExpStr -> asString += "  $subExpStr\n" }
        }
        return asString
    }
}