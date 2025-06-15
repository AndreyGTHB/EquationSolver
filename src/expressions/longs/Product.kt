package expressions.longs

import expressions.*
import expressions.binary.Quotient
import expressions.monomials.Monomial
import expressions.number.Rational
import expressions.number.Real
import org.slf4j.LoggerFactory
import utils.power
import utils.toRational

class Product private constructor(body: List<Expression>, final: Boolean) : LongExpression(body, final) {
    private val logger = LoggerFactory.getLogger(Product::class.java)

    constructor(body: List<Expression>) : this(body, false)

    override suspend fun simplify(): Expression {
        return simplify(true)
    }
    suspend fun simplify(expandBrackets: Boolean): Expression {
        if (final && !expandBrackets) return this
//        logger.trace("Product.simplify()")

        val simpleThis = simplifySoftly()
        val simpleBody = simpleThis.body

        if (expandBrackets) {
            val expanded = simpleThis.expandBrackets()
            if (expanded.body.size > 1) return expanded.simplify()
        }

        simpleBody.forEachIndexed { i, factor ->
            if (factor is Quotient) {
                val numerBody = mutableListOf(factor.numer)
                numerBody += simpleBody.slice(0 until i)
                numerBody += simpleBody.slice(i+1 until simpleBody.size)
                val numerProduct = Product(numerBody)
                return Quotient(numerProduct to factor.denom).simplify()
            }
        }

        return when (simpleBody.size) {
            0 -> unit()
            1 -> simpleBody.first()
            else -> simpleThis
        }
    }
    private suspend fun simplifySoftly(): Product {
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
        var rationalFactor = unit()
        val realBaseMap = mutableMapOf<Int, Rational>()
        var monomialFactor = unitMonomial()
        var quotientFactor = unitQuotient()
        prevBody.forEach { factor ->
            when (factor) {
                is Rational -> {
                    if (factor.isZero()) return zeroProduct()
                    rationalFactor *= factor
                }
                is Real ->     realBaseMap[factor.base] = (realBaseMap[factor.base] ?: zero()) + factor.exponent
                is Monomial -> monomialFactor *= factor
                is Quotient -> quotientFactor *= factor
                else        -> currBody.add(factor)
            }
        }

        // Continue simplifying the real numbers
        val realFactors = mutableListOf<Real>()
        for((base, exp) in realBaseMap) {
            val (sRational, sReal) = base.power(exp).simplifyAndSeparate()
            rationalFactor *= sRational
            if (!sReal.isUnit()) realFactors.add(sReal)

        }
        val rootsMap = mutableMapOf<Int, Int>()
        realFactors.forEach { factor ->
            val (intExp, rootIndex) = factor.exponent.body
            rootsMap[rootIndex] = (rootsMap[rootIndex] ?: 1) * factor.base.power(intExp)
        }
        for((index, base) in rootsMap) {
            val rootExp = index.toRational().flip()
            val (sRational, sReal) = base.power(rootExp).simplifyAndSeparate()
            rationalFactor *= sRational
            currBody.add(sReal)
        }
        arrayOf(rationalFactor, monomialFactor, quotientFactor).forEach {
            val sFactor = it.simplify()
            if (!sFactor.isUnitRational()) currBody.add(sFactor)
        }

        currBody.sort()
        return Product(currBody, true)
    }

    private fun expandBrackets(): Sum {
        val expandedBody = mutableListOf<Sum>()
        for ((i, factor) in body.withIndex()) {
            if (factor !is Sum) continue
            factor.body.forEach {
                val productBody = body.slice(0 until i) +
                                  it                                +
                                  body.slice(i+1 until body.size)
                expandedBody.add(Product(productBody).expandBrackets())
            }
            break
        }
        if (expandedBody.isEmpty()) return Sum(listOf(this))
        val currBody = mutableListOf<Expression>()
        expandedBody.forEach { sum -> sum.body.forEach { term -> currBody.add(term) } }
        return Sum(currBody)
    }
    private suspend fun factorOutSums(): Product {
        val newBody = mutableListOf<Expression>()
        body.forEach {
            if (it is Sum) {
                val cf = it.commonInternalFactor()
                if (cf !is Rational) {
                    newBody.add(cf)
                    newBody.add(it.reduce(cf))
                }
                else newBody.add(it)
            }
            else newBody.add(it)
        }
        return Product(newBody)
    }

    override suspend fun commonFactor(other: Expression): Expression? {
        return when (other) {
            is Real     -> commonFactorWithReal(other)
            is Monomial -> commonFactorWithMonomial(other)
            is Product  -> commonFactorWithProduct(other)
            else        -> null
        }
    }
    private suspend fun commonFactorWithReal(other: Real): Product {
        val cfBody = mutableListOf<Expression>()
        var currOther: Expression = other
        body.forEach {
            val cf = commonFactor(it, currOther)
            if (cf is Rational) return@forEach
            cfBody.add(cf)
            currOther = currOther.reduce(cf)
        }
        return Product(cfBody)
    }
    private suspend fun commonFactorWithMonomial(other: Monomial): Expression? {
        body.forEach { if (it is Monomial) return commonFactor(it, other) }
        return null
    }

    private suspend fun commonFactorWithProduct(other: Product): Expression {
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

    override suspend fun _reduceOrNull(other: Expression): Expression? {
        if (other is Rational) {
            return this * other.flip()
        }

        val newBody = mutableListOf<Expression>()
        var currOther = other
        body.forEach {
            if (currOther is Rational) return@forEach
            val cf = commonFactor(it, currOther)
            newBody.add(it.reduce(cf))
            currOther = currOther.reduce(cf)
        }
        if (currOther is Rational) return Product(newBody) * (currOther as Rational).flip()
        return null
    }

    override fun rationalPart(): Rational {
        if (!final) TODO("Must be simplified")
        return if (body[0] is Rational) body[0] as Rational
          else                          unit()
    }
    override fun nonRationalPart(): Expression {
        if (!final) TODO("Must be simplified")
        return if (body[0] !is Rational) this
          else if (body.size == 1)       unit()
          else if (body.size == 2)       body[1]
          else                           Product(body.slice(1 until body.size), true)
    }
    override fun numericalPart(): Expression {
        if (!final) TODO("Must be simplified")
        val numPartSize = body.indexOfFirst { !it.isNumber() }
        return when (numPartSize) {
            -1   -> this
            0    -> unit()
            1    -> body[0]
            else -> Product(body.slice(0 until numPartSize), true)
        }
    }
    override fun nonNumericalPart(): Expression {
        if (!final) TODO("Must be simplified")
        val numPartSize = body.indexOfFirst { !it.isNumber() }
        return when (numPartSize) {
            -1          -> unit()
            0           -> this
            body.size-1 -> body.last()
            else        -> Product(body.slice(numPartSize until body.size), true)
        }
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