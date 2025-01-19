package expressions.longs

import expressions.Expression

abstract class LongExpression(override val body: List<Expression>) : Expression() {
    protected fun simplifiedBody(): List<Expression> {
        val newBody = body.map { it.simplified() }
            .toList()
        return newBody
    }

    override fun toString(): String {
        return body.toString()
    }
}