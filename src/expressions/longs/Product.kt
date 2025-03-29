package expressions.longs

import expressions.Expression
import expressions.commonFactor
import expressions.monomials.Monomial
import expressions.numerical.Fraction
import expressions.unitFraction
import expressions.unitMonomial
import utils.toFraction

class Product private constructor(body: List<Expression>, final: Boolean) : LongExpression(body, final) {
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

    override fun commonFactor(other: Expression): Expression? {
        return when (other) {
            is Monomial -> commonFactorWithMonomial(other)
            is Product -> commonFactorWithProduct(other)
            else -> null
        }
    }
    private fun commonFactorWithMonomial(other: Monomial): Monomial? {
        var cf = unitMonomial()
        var currOther = other
        for (it in body) {
            if (it is Monomial) {
                val currcf = commonFactor(it, currOther)
                if (currcf !is Monomial) continue
                cf *= currcf
                val reduced = currOther.reduceOrNull(currOther)
                if (reduced !is Monomial) return cf
                currOther = reduced
            }
        }
        if (cf.varMap.isEmpty()) return null
        return cf
    }
    private fun commonFactorWithProduct(other: Product): Expression {
        val cfBody = mutableListOf<Expression>()
        val currOtherBody = other.body.toMutableList()
        this.body.forEach { fact1 ->
            var currFact1 = fact1
            currOtherBody.forEachIndexed { i, fact2 ->
                val factCf = commonFactor(currFact1, fact2)
                if (factCf !is Fraction) {
                    cfBody.add(factCf)
                    currFact1 = currFact1.reduceOrNull(factCf)!!
                    currOtherBody[i] = currOtherBody[i].reduceOrNull(factCf)!!
                }
            }
        }
        return Product(cfBody).simplify()
    }

    override fun reduceOrNull(other: Expression): Expression? {
        if (!final) throw RuntimeException("A non-simplified product cannot be reduced")
        if (other is Fraction) {
            return (this * other.flip()).simplify()
        }

        val newBody = mutableListOf<Expression>()
        var currOther = other
        body.forEach {
            val cf = commonFactor(it, currOther)
            newBody.add(it.reduceOrNull(cf)!!)
            currOther = currOther.reduceOrNull(cf)!!
        }
        if (currOther is Fraction) return Product(newBody).simplify()
        return null
    }

    override operator fun times(other: Expression): Product {
        return Product(listOf(other) + body)
    }
    override operator fun unaryMinus(): Product {
        val newBody = mutableListOf(-body.first())
        newBody += body.slice(1 until body.size)
        return Product(newBody)
    }
}