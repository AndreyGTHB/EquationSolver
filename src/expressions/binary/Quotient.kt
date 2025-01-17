package expressions.binary

import expressions.Expression

open class Quotient(body: Pair<Expression, Expression>) : BinaryExpression(body) {
    override fun simplified(): Expression {
        val numerator = body.first
        val denominator = body.second
        return this
    }
}