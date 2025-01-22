package expressions.binary

import expressions.Expression
import expressions.numerical.NumFraction

abstract class BinaryExpression (override val body: Pair<Expression, Expression>) : Expression() {
    protected fun simplifiedBody(): Pair<Expression, Expression> {
        return body.first.simplified() to body.second.simplified()
    }
    protected fun emptyBody(): Pair<Expression, Expression> {
        return NumFraction(0 to 1) to NumFraction(0 to 1)
    }
}