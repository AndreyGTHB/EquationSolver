package expressions.number

import equations.Domain
import equations.FullDomain
import expressions.Expression
import expressions.longs.Product
import expressions.unit
import expressions.unitReal
import utils.*

class Real (
    override val body: Pair<Int, Rational>,
    domain: Domain = FullDomain,
) : Expression(domain, false) {
    val base = body.first
    val exponent = body.second

    override val isNumber = true

    init {
        if (base == 0 && !exponent.isPositive()) TODO("Division by zero")
        if (base < 0 && !exponent.isInteger()) TODO("Domain of definition")
    }

    override fun
            _simplify(): Expression {
        if (exponent.isNegative()) {
            val opposite = Real(base to -exponent)
            return (unit() / opposite).simplify()
        }

        val sReal = simplifySoftly()
        if (sReal.base == 0 || sReal.base == 1) return sReal.base.toRational()
        val (intExponent, rootIndex) = sReal.exponent.body
        if (rootIndex == 1) return base.power(intExponent).toRational()

        val baseDecomp = base.factorise().mapValues { it.value * intExponent }

        var outerPart = 1
        var innerPart = 1
        for ((multipleBase, multipleExp) in baseDecomp) {
            val outerExp = multipleExp / rootIndex
            val innerExp = multipleExp % rootIndex
            if (outerExp != 0) outerPart *= multipleBase.power(outerExp)
            if (innerExp != 0) innerPart *= multipleBase.power(innerExp)
        }

        val innerPartDecompose = innerPart.factorise()
        val newIntExponent = innerPartDecompose.values.gcd()
        if (newIntExponent != 1) innerPart = innerPartDecompose.root(newIntExponent)
        val realFactor = Real(innerPart to (newIntExponent over rootIndex)).apply { final = true }
        val integerFactor = outerPart.toRational()

        return if (outerPart == 1) realFactor
          else if (innerPart == 1) integerFactor
          else                     integerFactor * realFactor
    }

    private fun simplifySoftly(): Real {
        val sExponent = exponent.simplify()
        return Real(base to sExponent)
    }

    private fun Map<Int, Int>.root(index: Int): Int {
        val reduced = mapValues{ (_, exp) -> exp / index }
        var result = 1
        reduced.forEach { (factor, exp) -> result *= factor.power(exp) }
        return result
    }

    fun simplifyAndSeparate(): Pair<Rational, Real> {
        return when (val sThis = simplify()) {
            is Rational -> sThis to unitReal()
            is Real     -> unit() to sThis
            else -> {
                sThis as Product
                val rationalMultiple = sThis.body[0] as Rational
                val realMultiple = sThis.body[1] as Real
                rationalMultiple to realMultiple
            }
        }
    }

    fun isUnit() = base == 1 || exponent.isZero()

    override fun _commonFactor(other: Expression): Real? {
        if (other !is Real) return null
        val commonBaseFactor = gcd(this.base, other.base)
        return Real(commonBaseFactor to min(this.exponent, other.exponent)).apply { final = true }
    }

    override fun _reduceOrNull(other: Expression): Expression? {
        if (other is Rational) return other.flip() * this
        if (other !is Real) return null
        if (this.base % other.base != 0 || this.exponent < other.exponent) return null
        val factor1 = other.base.power(this.exponent - other.exponent)
        val factor2 = (this.base / other.base).power(this.exponent)
        return factor1 * factor2
    }

    override fun toString(): String {
        val str = "$base^($exponent)"
        return str
    }
}


fun Int.power(exp: Rational) = Real(this to exp)
fun Int.squareRoot() = Real(this to (1 over 2))
