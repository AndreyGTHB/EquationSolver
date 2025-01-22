package expressions.longs

import expressions.Expression

class Product(body: List<Expression>) : LongExpression(body) {

    override fun simplified(): Expression {
        return this
    }

    override fun simplifiedSoftly(): Product { return this }


    override operator fun unaryMinus(): Sum {
        TODO("to implement")
    }
}