package expressions.longs

import expressions.*
import expressions.monomials.Monomial
import expressions.numerical.Fraction

class Product private constructor(body: List<Expression>, final: Boolean) : LongExpression(body, final) {
    constructor(body: List<Expression>) : this(body, false)

    companion object {
        fun nullProduct(): Product = Product(listOf(nullFraction()), true)
    }

    override fun simplify(): Expression {
        return simplify(false)
    }
    fun simplify(expandBrackets: Boolean): Expression {
        if (final) return this

        val simpleThis = simplifySoftly()
        val simpleBody = simpleThis.body
        return if (simpleBody.size == 1) simpleBody.first()
               else if (expandBrackets)  simpleThis.expandBrackets().simplify()
               else                      Product(simpleBody, true)
    }
    override fun simplifySoftly(): Product {
        if (final) return this

        var prevBody = simplifyBody()
        var currBody = mutableListOf<Expression>()
        prevBody.forEach {fact ->
            if (fact is Product) fact.body.forEach { subFact -> currBody.add(subFact) }
            else                                                currBody.add(fact)
        }

        prevBody = currBody
        currBody = mutableListOf()
        var numFactor = unitFraction()
        var monomialFactor = unitMonomial()
        prevBody.forEach { fact ->
            when (fact) {
                is Fraction -> {
                    if (fact.isNull()) return nullProduct()
                    numFactor *= fact
                }
                is Monomial -> monomialFactor *= fact
                else        -> currBody.add(fact)
            }
        }
        if (!numFactor.isUnit()) currBody.add(numFactor.simplify())
        if (!monomialFactor.isUnit()) currBody.add(monomialFactor.simplify())
        return Product(currBody)
    }

    private fun expandBrackets(): Expression {
        body.forEachIndexed { i, fact ->
            if (fact is Sum) {
                val expandedBody: MutableList<Expression> = mutableListOf()
                fact.body.forEach { term ->
                    val productBody = mutableListOf(term)
                    productBody += body.slice(0 until i)
                    productBody += body.slice(i+1 until body.size)
                    expandedBody.add(Product(productBody))
                }
                return Sum(expandedBody)
            }
        }
        return this
    }
    private fun factorOutSums(): Product {
        val newBody = mutableListOf<Expression>()
        body.forEach {
            if (it is Sum) {
                val cf = it.commonInternalFactor()
                if (cf !is Fraction) {
                    newBody.add(cf)
                    newBody.add(it.reduceOrNull(cf)!!)
                }
                else newBody.add(it)
            }
        }
        return Product(newBody)
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