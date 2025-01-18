package expressions.binary

import expressions.Expression

open class Quotient(body: Pair<Expression, Expression>) : BinaryExpression(body) {
    val numerator
        get() = _body.first
    val denominator
        get() = _body.second

    override fun simplified(): Expression {
        simplifyBody()
        return this
    }
}