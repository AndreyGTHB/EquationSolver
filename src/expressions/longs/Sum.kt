package expressions.longs

import expressions.Expression
import expressions.monomials.Monomial
import expressions.numerical.NumFraction

class Sum(body: List<Expression>) : LongExpression(body) {
    override fun simplified(): Expression {
        return this
    }

    operator fun plus(other: Expression):  Sum {
        return Sum(body + listOf(other))
    }

    override fun simplifiedSoftly(): Sum {
        val newBody = simplifiedBody()

        val monomialVarMaps: MutableMap<Map<Char, Int>, NumFraction>
        var currVarMap: Map<Char, Int> = mapOf()
        var currCoeff: NumFraction = NumFraction(0 to 1)
        for (exp in body) {
            if (exp is Monomial) {
                currVarMap = exp.varMap
//                currCoeff = exp.coefficient
//                monomialVarMaps[currVarMap] = monomialVarMaps.getOrDefault(currVarMap)
            }
        }
        return this
    }
}