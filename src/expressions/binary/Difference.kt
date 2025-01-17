package expressions.binary

import expressions.Expression

class Difference(body: Pair<Expression, Expression>): BinaryExpression(body) {
    override fun simplified(): Expression {
        return this
    }
}