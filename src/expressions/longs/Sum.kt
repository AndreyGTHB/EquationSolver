package expressions.longs

import expressions.Expression
import expressions.monomials.Monomial
import expressions.numerical.NumFraction

class Sum(body: List<Expression>) : LongExpression(body) {
    override fun simplified(): Expression {
        val simplifiedSum = simplifiedSoftly()
        return when (simplifiedSum.body.size) {
            0 -> NumFraction(0 to 1)
            1 -> simplifiedSum.body.first()
            else -> simplifiedSum
        }
    }

    override fun simplifiedSoftly(): Sum {
        val simplifiedBody = simplifiedBody()
        val newBody = mutableListOf<Expression>()
        var number = NumFraction(0 to 1)
        val varMaps: MutableMap<Map<Char, Int>, NumFraction> = mutableMapOf()

        for (exp in simplifiedBody) {
            when (exp) {
                is Monomial    -> {
                    val (coeff, vm) = exp.body
                    varMaps[vm] = (varMaps[vm] ?: NumFraction(0 to 1)) + coeff
                }
                is Sum         -> {
                    for (term in exp.body) { newBody.add(term) }
                }
                is NumFraction -> { number += exp }
                else           -> { newBody.add(exp) }
            }
        }

        newBody.add(number)
        varMaps.forEach { (vm, coeff) ->
            newBody.add(Monomial(coeff to vm))
        }
        return Sum(newBody)
    }

    fun factorOut(): Product {
        var commonFactor = body.first()
    }


    override operator fun unaryMinus(): Sum {
        val newBody = body.map { -it }.toList()
        return Sum(newBody)
    }

    override operator fun plus(other: Expression):  Sum {
        return Sum(body + listOf(other))
    }
}