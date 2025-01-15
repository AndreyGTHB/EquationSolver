package expressions.binary

import expressions.Expression
import expressions.NumberExp

class Product(body: Pair<Expression, Expression>) : BinaryExpression(body) {

    override fun simplify(): Expression {
        this.simplifyBody()

        val factor1 = body.first
        val factor2 = body.second
        when {
            factor1 is NumberExp && factor2 is NumberExp -> { return NumberExp(factor1.body * factor2.body) }

        }
        return this
    }
}