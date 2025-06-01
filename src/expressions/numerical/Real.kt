package expressions.numerical

import expressions.Expression
import utils.factorise
import utils.over
import utils.power
import utils.toRational

class Real private constructor(override val body: Pair<Int, Rational>, final: Boolean) : Expression(final) {
    constructor(body: Pair<Int, Rational>) : this(body, false)

    val base = body.first
    val exponent = body.second

    init {
        if (base < 0 && !exponent.isInteger()) TODO("Domain of definition")
    }

    override fun simplify(): Expression {
        if (final) return this

        val sReal = simplifySoftly()
        val (intExponent, rootIndex) = sReal.exponent.body
        if (rootIndex == 1) return (base over 1) raisedTo intExponent

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

    override fun toString(): String {
        val str = "$base^($exponent)"
        return str
    }
}