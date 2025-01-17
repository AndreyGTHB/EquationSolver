package expressions.longs

import expressions.Expression
import expressions.binary.BinaryExpression

class Sum(body: Pair<Expression, Expression>) : BinaryExpression(body) {

    override fun simplified(): Expression {
        val term1 = body.first
        val term2 = body.second
        return this
    }
}