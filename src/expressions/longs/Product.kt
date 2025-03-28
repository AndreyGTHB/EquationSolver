package expressions.longs

import expressions.Reducible
import expressions.Expression
import expressions.numerical.Fraction

class Product private constructor(body: List<Expression>, final: Boolean) : LongExpression(body, final), Reducible {
    constructor(body: List<Expression>) : this(body, false)

    override fun simplify(): Expression {
        if (final) return this
        val simplifiedProduct = simplifySoftly()

        // Expanding brackets
        for ((i, exp) in simplifiedProduct.body.withIndex()) {
            if (exp is Sum) {
                val expandedBody: MutableList<Expression> = mutableListOf()
                for (term in exp.body) {
                    val productBody = mutableListOf(term)
                    productBody += simplifiedProduct.body.slice(0 until i)
                    productBody += simplifiedProduct.body.slice(i+1 until simplifiedProduct.body.size)
                    expandedBody.add(Product(productBody))
                }
                return Sum(expandedBody).simplify()
            }
        }
        return Product(simplifiedProduct.body, true)
    }
    override fun simplifySoftly(): Product {
        return Product(simplifiedBody())
    }


    override operator fun times(other: Expression): Product {
        return Product(listOf(other) + body)
    }
    override operator fun unaryMinus(): Product {
        val newBody = mutableListOf(-body.first())
        newBody += body.slice(1 until body.size)
        return Product(newBody)
    }

    override fun reduceOrNull(other: Expression): Expression? {
        val simplifiedProduct = simplifySoftly()
        val oldBody = simplifiedProduct.body
        val newBody = mutableListOf<Expression>()

        if (other is Fraction) {
            val reciprocal = other.flip()
            return Product(listOf(reciprocal) + oldBody)
        }
        for ((i, fact) in oldBody.withIndex()) {
            if (fact is Reducible) {
                val divided = fact.reduceOrNull(other) ?: continue
                newBody += divided
                newBody += oldBody.slice(0 until i)
                newBody += oldBody.slice(i+1 until oldBody.size)
                return Product(newBody)
            }
        }
        return null
    }
}