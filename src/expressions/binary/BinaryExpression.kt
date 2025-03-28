package expressions.binary

import expressions.Expression
import expressions.numerical.Fraction

abstract class BinaryExpression (
    override val body: Pair<Expression, Expression>,
    final: Boolean
) : Expression(final) {
    protected fun simplifiedBody(): Pair<Expression, Expression> {
        return body.first.simplify() to body.second.simplify()
    }
    protected fun emptyBody(): Pair<Expression, Expression> {
        return Fraction(0 to 1) to Fraction(0 to 1)
    }
}