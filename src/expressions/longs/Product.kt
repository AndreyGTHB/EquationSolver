package expressions.longs

import expressions.Expression

class Product(body: List<Expression>) : LongExpression(body) {

    override fun simplified(): Expression {
        val simplifiedProduct = simplifiedSoftly()
        for ((i, exp) in simplifiedProduct.body.withIndex()) {
            if (exp is Sum) {
                val expandedBody: MutableList<Expression> = mutableListOf()
                for (term in exp.body) {
                    val productBody = mutableListOf(term)
                    productBody += simplifiedProduct.body.slice(0 until i)
                    productBody += simplifiedProduct.body.slice(i+1 until simplifiedProduct.body.size)
                    expandedBody.add(Product(productBody))
                }
                return Sum(expandedBody).simplified()
            }
        }
    }

    override fun simplifiedSoftly(): Product {
        return Product(simplifiedBody())
    }


    override operator fun unaryMinus(): Product {
        val newBody = mutableListOf(-body.first())
        newBody += body.slice(1 until body.size)
        return Product(newBody)
    }
}