package expressions.longs

import expressions.Expression

class Product(body: MutableList<Expression>) : LongExpression(body) {

    override fun simplified(): Expression {
        return this
    }
}