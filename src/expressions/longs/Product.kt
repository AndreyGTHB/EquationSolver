package expressions.longs

import equations.Domain
import equations.FullDomain
import expressions.*
import expressions.binary.Power
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

    override fun _simplify(): Expression { simplifySoftly().apply {
        val sumsCount = body.countSums()
        return when (sumsCount) {
            0    -> simplifyIgnoringSums()
            1    -> simplifyWithOneSum()
            else -> expandBrackets().simplify()
        }
    }}

    private fun simplifyWithOneSum(): Expression {
        val sumFactor = body.first { it is Sum }
        return if (sumFactor.isNumber) simplifyIgnoringSums()
        else                             expandBrackets().simplify()
    }

    private fun simplifyIgnoringSums(): Expression {
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
        val newBody = simplifyBody()
            .combinePowers()
            .expandProducts()
            .factorOutSumsIfNeeded()
            .combineByTypes()
            .sorted()
        return Product(newBody)
    }

    private fun List<Expression>.combinePowers(): List<Expression> {
        val newBody = emptyBody()
        val baseMap = mutableMapOf<Expression, Expression>()
        forEach {
            if (it is Power) baseMap[it.base] = (baseMap[it.base] ?: zero()) + it.exponent
            else             newBody.add(it)
        }

        baseMap.forEach { base, exp -> newBody.add(Power(base to exp).simplify()) }
        return newBody
    }

    private fun List<Expression>.expandProducts(): List<Expression> {
        val newBody = emptyBody()
        forEach { factor ->
            if (factor is Product) factor.body.forEach { subFactor -> newBody.add(subFactor) }
            else                                                      newBody.add(factor)
        }
        return newBody
    }

    private fun List<Expression>.factorOutSumsIfNeeded(): List<Expression> {
        val newBody = emptyBody()
        var firstSum = true
        forEach {
            if (it is Sum) {
                if (!firstSum) return this
                firstSum = false

                val cif = it.commonInternalFactor
                newBody.add(cif)
                newBody.add(it.reduce(cif))
            }
            else newBody.add(it)
        }
        return newBody
    }

    private fun List<Expression>.combineByTypes(): List<Expression> {
        val newBody = emptyBody()
        var rationalFactor = unit()
        val realFactors1 = mutableListOf<Real>()
        val powerBaseMap = mutableMapOf<Expression, Expression>()
        var monomialFactor = unitMonomial()
        var quotientFactor = unitQuotient()
        forEach {
            when (it) {
                is Rational -> {
                    if (it.isZero()) return listOf(zero())
                    rationalFactor *= it
                }
                is Real ->     realFactors1.add(it)
                is Power    -> powerBaseMap[it.base] = (powerBaseMap[it.base] ?: zero()) + it.exponent
                is Monomial -> monomialFactor *= it
                is Quotient -> quotientFactor *= it
                else        -> newBody.add(it)
            }
        }

        var realFactors2: List<Real> = realFactors1
        realFactors2.combineByBases().also { (extractedRational, combined) ->
            rationalFactor *= extractedRational
            realFactors2 = combined
        }
        realFactors2.combineByExponents().also { (extractedRational, combined) ->
            rationalFactor *= extractedRational
            realFactors2 = combined
        }
        newBody.addAll(realFactors2)

        arrayOf(rationalFactor, monomialFactor, quotientFactor).forEach {
            val sFactor = it.simplify()
            if (!sFactor.isUnitRational()) newBody.add(sFactor)
        }
        return newBody
    }

    private fun List<Real>.combineByBases(): Pair<Rational, List<Real>> {
        val basesMap = mutableMapOf<Int, Rational>()
        forEach { basesMap[it.base] = (basesMap[it.base] ?: zero()) + it.exponent }

        val combined = mutableListOf<Real>()
        var extractedRational = unit()
        basesMap.forEach { base, exp ->
            val (sRational, sReal) = base.power(exp).simplifyAndSeparate()
            extractedRational *= sRational
            if (!sReal.isUnit()) combined.add(sReal)
        }

        return extractedRational to combined
    }

    private fun List<Real>.combineByExponents(): Pair<Rational, List<Real>> {
        val rootsMap = mutableMapOf<Int, Int>()
        forEach {
            val (intExp, rootIndex) = it.exponent.body
            rootsMap[rootIndex] = (rootsMap[rootIndex] ?: 1) * it.base.power(intExp)
        }

        val combined = mutableListOf<Real>()
        var extractedRational = unit()
        rootsMap.forEach { index, base ->
            val rootExp = index.toRational().flip()
            val (sRational, sReal) = base.power(rootExp).simplifyAndSeparate()
            extractedRational *= sRational
            combined.add(sReal)
        }

        return extractedRational to combined
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
        val currBody = emptyBody()
        expandedBody.forEach { sum -> sum.body.forEach { term -> currBody.add(term) } }
        return Sum(currBody)
    }

    private fun List<Expression>.countSums() = count { it is Sum }

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
        val cfBody = emptyBody()
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
        val cfBody = emptyBody()
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

        val newBody = emptyBody()
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
        val numPartSize = body.indexOfFirst { !it.isNumber }
        return when (numPartSize) {
            -1   -> this
            0    -> unit()
            1    -> body[0]
            else -> Product(body.slice(0 until numPartSize)).apply { final = true }
        }
    }
    override fun nonNumericalPart(): Expression {
        assert(final)
        val numPartSize = body.indexOfFirst { !it.isNumber }
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
    override operator fun unaryMinus(): Expression {
        if (body.isEmpty()) return -unit()

        val newBody = body.toMutableList()
        newBody[0] = -newBody[0]
        return Product(newBody)
    }
}