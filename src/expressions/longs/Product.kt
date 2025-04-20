package expressions.longs

import expressions.*
import expressions.binary.Quotient
import expressions.monomials.Monomial
import expressions.numerical.Rational

class Product private constructor(body: List<Expression>, final: Boolean) : LongExpression(body, final) {
    constructor(body: List<Expression>) : this(body, false)

    override fun simplify(): Expression {
        return simplify(true)
    }
    fun simplify(expandBrackets: Boolean): Expression {
        if (final && !expandBrackets) return this

        val simpleThis = simplifySoftly()
        val simpleBody = simpleThis.body

        if (expandBrackets) {
            val expanded = expandBrackets()
            if (expanded.body.size > 1) return expanded.simplify()
        }

        simpleBody.forEachIndexed { i, fact ->
            if (fact is Quotient) {
                val numerBody = mutableListOf(fact.numer)
                numerBody += simpleBody.slice(0 until i)
                numerBody += simpleBody.slice(i+1 until simpleBody.size)
                val numerProduct = Product(numerBody)
                return Quotient(numerProduct to fact.denom).simplify()
            }
        }

        return if (simpleBody.size == 1) simpleBody.first()
               else                      Product(simpleBody, true)
    }
    override fun simplifySoftly(): Product {
        if (final) return this

        // Associativity
        var prevBody = simplifyBody()
        var currBody = mutableListOf<Expression>()
        prevBody.forEach { fact ->
            if (fact is Product) fact.body.forEach { subFact -> currBody.add(subFact) }
            else                                                currBody.add(fact)
        }

        // Extracting common factors from the sums
        prevBody = currBody
        currBody = Product(prevBody)
            .factorOutSums()
            .body.toMutableList()

        // Simplifying
        prevBody = currBody
        currBody = mutableListOf()
        var monomialFactor = unitMonomial()
        var quotientFactor = unitQuotient()
        prevBody.forEach { fact ->
            when (fact) {
                is Rational -> {
                    if (fact.isNull()) return zeroProduct()
                    monomialFactor *= fact
                }
                is Monomial -> monomialFactor *= fact
                is Quotient -> quotientFactor *= fact
                else        -> currBody.add(fact)
            }
        }
        val simpleMf = monomialFactor.simplify()
        val simpleQf = quotientFactor.simplify()
        if (!simpleMf.isUnitRational()) currBody.add(simpleMf)
        if (!simpleQf.isUnitRational()) currBody.add(simpleQf)

        currBody.sort()
        return Product(currBody)
    }

    private fun expandBrackets(): Sum {
        body.forEachIndexed { i, fact ->
            if (fact is Sum) {
                val expandedBody: MutableList<Expression> = mutableListOf()
                fact.body.forEach { term ->
                    val productBody = mutableListOf(term)
                    productBody += body.slice(0 until i)
                    productBody += body.slice(i+1 until body.size)
                    expandedBody.add(Product(productBody).expandBrackets())
                }
                return Sum(expandedBody)
            }
        }
        return Sum(listOf(this))
    }
    private fun factorOutSums(): Product {
        val newBody = mutableListOf<Expression>()
        body.forEach {
            if (it is Sum) {
                val cf = it.commonInternalFactor()
                if (cf !is Rational) {
                    newBody.add(cf)
                    newBody.add(it.reduce(cf))
                }
            }
            else newBody.add(it)
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
    private fun commonFactorWithMonomial(other: Monomial): Monomial {
        var cf = unitMonomial()
        var currOther = other
        for (it in body) {
            if (it is Monomial) {
                val currcf = commonFactor(it, currOther)
                if (currcf !is Monomial) continue
                cf *= currcf
                val reduced = currOther.reduceOrNull(currcf)
                if (reduced !is Monomial) return cf
                currOther = reduced
            }
        }
        return cf
    }
    private fun commonFactorWithProduct(other: Product): Expression {
        val cfBody = mutableListOf<Expression>()
        val currOtherBody = other.body.toMutableList()
        this.body.forEach { fact1 ->
            var currFact1 = fact1
            currOtherBody.forEachIndexed { i, fact2 ->
                val factCf = commonFactor(currFact1, fact2)
                if (factCf !is Rational) {
                    cfBody.add(factCf)
                    currFact1 = currFact1.reduce(factCf)
                    currOtherBody[i] = currOtherBody[i].reduce(factCf)
                }
            }
        }
        return Product(cfBody)
    }

    override fun reduceOrNull(other: Expression): Expression? {
        if (!final) throw RuntimeException("A non-simplified product cannot be reduced")
        if (other is Rational) {
            return (this * other.flip()).simplify()
        }

        val newBody = mutableListOf<Expression>()
        var currOther = other
        body.forEach {
            val cf = commonFactor(it, currOther)
            newBody.add(it.reduce(cf))
            currOther = currOther.reduce(cf)
        }
        if (currOther is Rational) return Product(newBody, true)
                                              .reduce(currOther)
                                              .simplify()
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