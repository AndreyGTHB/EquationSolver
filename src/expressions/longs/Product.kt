package expressions.longs

import equations.Domain
import equations.FullDomain
import expressions.*
import expressions.binary.Quotient
import expressions.monomials.Monomial
import expressions.number.Rational
import expressions.number.Real
import expressions.number.power
import expressions.number.toRational
import utils.power

class Product (
    body: List<Expression>,
    domain: Domain = FullDomain,
) : LongExpression(body, domain, false) {
    constructor(vararg body: Expression) : this(body.toList())

    override fun _simplify(): Expression {
        val sThis = simplifySoftly()
        val sBody = sThis.body

        val sumsCount = sBody.countSums()
        if (sumsCount == 0) return sThis.simplifyIgnoringSums()
        if (sumsCount > 1)  return sThis.expandBrackets().simplify()

        // If exactly one sum in the body
        val sumFactor = sBody.first { it is Sum }
        return if (sumFactor.isNumber()) sThis.simplifyIgnoringSums()
               else                      sThis.expandBrackets().simplify()
    }
    private fun simplifyIgnoringSums(): Expression {
        // Calling only after simplifySoftly()

        body.forEachIndexed { i, factor -> if (factor is Quotient) {
            val numerBody = buildList {
                addAll(body.slice(0 until i))
                add(factor.numer)
                addAll(body.slice(i + 1 until body.size))
            }
            val numerProduct = Product(numerBody)
            return Quotient(numerProduct to factor.denom).simplify()
        }}

        return when (body.size) {
            0 -> unit()
            1 -> body[0]
            else -> this
}
    }
    private fun simplifySoftly(): Product {
        // Associativity
        var prevBody = simplifyBody()
        var currBody = mutableListOf<Expression>()
        prevBody.forEach { factor ->
            if (factor is Product) factor.body.forEach { subFact -> currBody.add(subFact) }
            else                                                    currBody.add(factor)
        }

        if (currBody.countSums() == 1) currBody = currBody.factorOutSums()

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
        realBaseMap.forEach { base, exp ->
            val (sRational, sReal) = base.power(exp).simplifyAndSeparate()
            rationalFactor *= sRational
            if (!sReal.isUnit()) realFactors.add(sReal)

        }
        val rootsMap = mutableMapOf<Int, Int>()
        realFactors.forEach { factor ->
            val (intExp, rootIndex) = factor.exponent.body
            rootsMap[rootIndex] = (rootsMap[rootIndex] ?: 1) * factor.base.power(intExp)
        }
        rootsMap.forEach { index, base ->
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
        return Product(currBody)
    }

    fun expandBrackets(): Sum {
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

    private fun List<Expression>.countSums(): Int = count { it is Sum }
    private fun List<Expression>.factorOutSums(): MutableList<Expression> {
        val result = mutableListOf<Expression>()
        forEach {
            if (it is Sum) {
                val cf = it.commonInternalFactor
                if (!cf.isUnitRational()) {
                    result.add(cf)
                    result.add(it.reduce(cf))
                }
                else result.add(it)
            }
            else result.add(it)
        }
        return result
    }

    override fun _commonFactor(other: Expression): Expression? {
        return when (other) {
            is Rational -> commonFactor(other, body[0])
            is Real     -> commonFactorWithReal(other)
            is Monomial -> commonFactorWithMonomial(other)
            is Product  -> commonFactorWithProduct(other)
            else        -> null
        }
    }
    private fun commonFactorWithReal(other: Real): Product {
        val cfBody = mutableListOf<Expression>()
        var currOther: Expression = other
        body.forEach {
            val cf = commonFactor(it, currOther)
            if (cf.isUnitRational()) return@forEach
            cfBody.add(cf)
            currOther = currOther.reduce(cf)
        }
        return Product(cfBody)
    }
    private fun commonFactorWithMonomial(other: Monomial): Expression? {
        body.forEach { if (it is Monomial) return commonFactor(it, other) }
        return null
    }
    private fun commonFactorWithProduct(other: Product): Expression {
        val cfBody = mutableListOf<Expression>()
        val currOtherBody = other.body.toMutableList()
        this.body.forEach { fact1 ->
            var currFact1 = fact1
            currOtherBody.forEachIndexed { i, fact2 ->
                val factCf = commonFactor(currFact1, fact2)
                if (factCf.isUnitRational()) return@forEachIndexed
                cfBody.add(factCf)
                currFact1 = currFact1.reduce(factCf)
                currOtherBody[i] = currOtherBody[i].reduce(factCf)
            }
        }
        return Product(cfBody)
    }

    override fun _reduceOrNull(other: Expression): Expression? {
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
        assert(final)
        return if (body[0] is Rational) body[0] as Rational
          else                          unit()
    }
    override fun nonRationalPart(): Expression {
        assert(final)
        return if (body[0] !is Rational) this
          else if (body.size == 1)       unit()
          else if (body.size == 2)       body[1]
          else                           Product(body.slice(1 until body.size)).apply { final = true }
    }
    override fun numericalPart(): Expression {
        assert(final)
        val numPartSize = body.indexOfFirst { !it.isNumber() }
        return when (numPartSize) {
            -1   -> this
            0    -> unit()
            1    -> body[0]
            else -> Product(body.slice(0 until numPartSize)).apply { final = true }
        }
    }
    override fun nonNumericalPart(): Expression {
        assert(final)
        val numPartSize = body.indexOfFirst { !it.isNumber() }
        return when (numPartSize) {
            -1          -> unit()
            0           -> this
            body.size-1 -> body.last()
            else        -> Product(body.slice(numPartSize until body.size)).apply { final = true }
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