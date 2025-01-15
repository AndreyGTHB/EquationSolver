package expressions.binary

import expressions.Expression
import expressions.NumberExp

class Fraction(body: Pair<Expression, Expression>) : BinaryExpression(body) {
    override fun simplify(): Expression {
        this.simplifyBody()

        val numerator = body.first
        val denominator = body.second
        when {
            denominator is NumberExp && denominator.body == 1 -> { return numerator }
        }
        return this
    }
}