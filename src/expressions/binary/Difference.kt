package expressions.binary

import expressions.Expression
import expressions.NumberExp

class Difference(body: Pair<Expression, Expression>): BinaryExpression(body) {
    override fun simplify(): Expression {
        this.simplifyBody()

        val minuend = body.first
        val subtrahend = body.second
        when {
            minuend is NumberExp && subtrahend is NumberExp -> { return NumberExp(minuend.body - subtrahend.body) }

        }
        return this
    }
}