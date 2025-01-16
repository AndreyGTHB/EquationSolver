package expressions.longs

import expressions.Expression
import expressions.numerical.Integer
import expressions.binary.BinaryExpression

class Sum(body: Pair<Expression, Expression>) : BinaryExpression(body) {

    override fun simplify() {
        val term1 = body.first
        val term2 = body.second
    }
}