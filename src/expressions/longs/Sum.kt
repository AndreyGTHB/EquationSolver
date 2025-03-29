package expressions.longs

import expressions.Expression
import expressions.ReducibleExpression
import expressions.monomials.Monomial
import expressions.numerical.Fraction
import utils.toFraction

class Sum private constructor(body: List<Expression>, final: Boolean) : LongExpression(body, final) {
    constructor(body: List<Expression>) : this(body, false)

    override fun simplify(): Expression {
        if (final) return this

        val simpleSum = simplifySoftly()
        return when (simpleSum.body.size) {
            0 -> Fraction(0 to 1)
            1 -> simpleSum.body.first()
            else -> Sum(simpleSum.body, true)
        }
    }

    override fun simplifySoftly(): Sum {
        if (final) return this

        val newBody = simplifiedBody().toMutableList()
        var freeTerm = Fraction(0 to 1)
        val varMaps: MutableMap<Map<Char, Int>, Fraction> = mutableMapOf()

        // Associativity
        for ((i, exp) in newBody.withIndex()) { // A bad moment
            if (exp is Sum) {
                newBody.removeAt(i)
                exp.body.forEach { newBody.add(it) }
            }
            else newBody.add(exp)
        }
        // Reduction of similar terms
        for (exp in newBody) {
            when (exp) {
                is Monomial    -> {
                    val (coeff, vm) = exp.body
                    varMaps[vm] = (varMaps[vm] ?: Fraction(0 to 1)) + coeff
                }
                is Sum         -> {
                    for (term in exp.body) { newBody.add(term) }
                }
                is Fraction -> { freeTerm += exp }
                else           -> { newBody.add(exp) }
            }
        }

        freeTerm = freeTerm.simplify()
        if (!freeTerm.isNull()) newBody.add(freeTerm)
        varMaps.forEach { (vm, coeff) ->
            newBody.add(Monomial(coeff to vm))
        }
        return Sum(newBody)
    }

    fun factorOut(): Expression {
        var newCF: Expression = 0.toFraction()
        body.forEach {
            if (it is ReducibleExpression) newCF = it.commonFactor(newCF)
        }
        return newCF
    }


    override operator fun unaryMinus(): Sum {
        val newBody = body.map { -it }.toList()
        return Sum(newBody)
    }

    override operator fun plus(other: Expression):  Sum {
        return Sum(body + listOf(other))
    }
}