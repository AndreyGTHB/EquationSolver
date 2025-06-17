package expressions.number

import expressions.Expression
import expressions.longs.Product
import expressions.unit
import expressions.unitReal
import utils.*

class Real private constructor(override val body: Pair<Int, Rational>, final: Boolean) : Expression(final) {
    constructor(body: Pair<Int, Rational>) : this(body, false)

    val base = body.first
    val exponent = body.second

    init {
        if (base == 0 && !exponent.isPositive()) TODO("Division by zero")
        if (base < 0 && !exponent.isInteger()) TODO("Domain of definition")
    }

    override fun _isNumber() = true
    override fun numericalPart() = this
    override fun nonNumericalPart() = unit()

    override fun simplify(): Expression {
        if (final) return this

        if (exponent.isNegative()) {
            val opposite = Real(base to -exponent)
            return (unit() / opposite).simplify()
        }

        val sReal = simplifySoftly()
        if (sReal.base == 0 || sReal.base == 1) return sReal.base.toRational()
        val (intExponent, rootIndex) = sReal.exponent.body
        if (rootIndex == 1) return base.power(intExponent).toRational()

        val decomp = base.factorise().mapValues { intExponent * it.value }

        var outerPart = 1
        var innerPart = 1
        for ((multipleBase, multipleExp) in decomp) {
            val outerExp = multipleExp / rootIndex
            val innerExp = multipleExp % rootIndex
            if (outerExp != 0) outerPart *= multipleBase.power(outerExp)
            if (innerExp != 0) innerPart *= multipleBase.power(innerExp)
        }

        return if (outerPart == 1) sReal
          else if (innerPart == 1) outerPart.toRational()
          else {
              val radical = Real(innerPart to rootIndex.toRational().flip())
              val asProduct = outerPart.toRational() * radical
              asProduct.simplify()
          }
    }
    private fun simplifySoftly(): Real {
        val sExponent = exponent.simplify()
        return Real(base to sExponent, true)
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

    override fun commonFactor(other: Expression): Real? {
        if (other !is Real) return null
        val commonBaseFactor = gcd(this.base, other.base)
        return Real(commonBaseFactor to min(this.exponent, other.exponent), true)
    }

    override fun _reduceOrNull(other: Expression): Expression? {
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
