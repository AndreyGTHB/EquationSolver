package expressions

import expressions.binary.BinaryExpression

open class Quotient(body: Pair<Expression, Expression>) : BinaryExpression(body) {
    override fun simplify() {
        val numerator = body.first
        val denominator = body.second
    }
}