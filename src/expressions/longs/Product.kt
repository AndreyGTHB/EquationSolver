package expressions.longs

import expressions.Expression

class Product(body: List<Expression>) : LongExpression(body) {

    override fun simplified(): Expression {
        return this
    }

    override fun simplifiedSoftly(): Product { return this }


    override operator fun unaryMinus(): Product {
        val newBody = mutableListOf(-body.first())
        newBody += body.slice(1 until body.size)
        return Product(newBody)
    }
}