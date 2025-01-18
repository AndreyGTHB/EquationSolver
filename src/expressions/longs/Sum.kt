package expressions.longs

import expressions.Expression
import expressions.monomials.Monomial
import expressions.numerical.NumFraction

class Sum(body: Collection<Expression>) : LongExpression(body) {
    override fun simplified(): Expression {
        return this
    }

    operator fun plus(other: Expression):  Sum {
        return Sum(_body + listOf(other))
    }

//    operator fun minus(other: NumFraction): Sum {
//        val opposite = NumFraction(0 - other.numerator to other.denominator)
//        return this + opposite
//    }
//    operator fun minus(other: Monomial): Sum {
//        val opposite = Monomial(-other.coefficient to other.variables)
//    }
}