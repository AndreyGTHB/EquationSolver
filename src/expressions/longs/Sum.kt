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
            else -> { return simplifiedSum }
        }
    }

    override fun simplifiedSoftly(): Sum {
        val simplifiedBody = simplifiedBody()
        val newBody = mutableListOf<Expression>()
        val varMaps: MutableMap<Map<Char, Int>, NumFraction> = mutableMapOf()

        for (exp in simplifiedBody) {
            if (exp !is Monomial) { newBody.add(exp); continue }
            val (coeff, vm) = exp.body
            varMaps[vm] = varMaps.getOrDefault(vm, NumFraction(0 to 1)) + coeff
        }
        varMaps.forEach { (vm, coeff) ->
            newBody.add(Monomial(coeff to vm))
        }

        return Sum(newBody)
    }


    override operator fun plus(other: Expression):  Sum {
        return Sum(body + listOf(other))
    }
}