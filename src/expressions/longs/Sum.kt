package expressions.longs

import expressions.Expression
import expressions.monomials.Monomial
import expressions.numerical.Fraction

class Sum private constructor(body: List<Expression>, final: Boolean) : LongExpression(body, final) {
    constructor(body: List<Expression>) : this(body, false)

    override fun simplify(): Expression {
        if (final) return this

        val simplifiedSum = simplifySoftly()
        return when (simplifiedSum.body.size) {
            0 -> Fraction(0 to 1)
            1 -> simplifiedSum.body.first()
            else -> Sum(simplifiedSum.body, true)
        }
    }

    override fun simplifySoftly(): Sum {
        if (final) return this

        val simplifiedBody = simplifiedBody()
        val newBody = mutableListOf<Expression>()
        var freeTerm = Fraction(0 to 1)
        val varMaps: MutableMap<Map<Char, Int>, Fraction> = mutableMapOf()

        for (exp in simplifiedBody) {
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

//    fun factorOut(): Product {
//        var commonFactor = body.first()
//    }


    override operator fun unaryMinus(): Sum {
        val newBody = body.map { -it }.toList()
        return Sum(newBody)
    }

    override operator fun plus(other: Expression):  Sum {
        return Sum(body + listOf(other))
    }
}