package expressions.binary

import expressions.Expression

open class Quotient(body: Pair<Expression, Expression>) : BinaryExpression(body) {
    val numerator
        get() = body.first
    val denominator
        get() = body.second

    override fun simplified(): Expression {
        simplifiedSoftly()
        return this
    }

    override fun simplifiedSoftly(): Quotient { return this }
}