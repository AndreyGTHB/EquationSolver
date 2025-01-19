package expressions.binary

import expressions.Expression

abstract class BinaryExpression (override val body: Pair<Expression, Expression>) : Expression() {
    protected fun simplifiedBody(): Pair<Expression, Expression> {
        return body.first.simplified() to body.second.simplified()
    }
}