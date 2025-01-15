package expressions.binary

import expressions.*

class Sum(body: Pair<Expression, Expression>) : BinaryExpression(body) {

    override fun simplify(): Expression {
        this.simplifyBody()

        val term1 = body.first
        val term2 = body.second
        when {
            term1 is NumberExp && term2 is NumberExp -> { return NumberExp(term1.body + term2.body) }

        }
        return this
    }
}