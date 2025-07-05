package statements

import expressions.Expression

class NotEqualsTo (variable: Char, expr: Expression) : Statement(variable, expr) {
    override fun simplify() = NotEqualsTo(variable, expr.simplify())
    override fun unaryMinus() = variable equalsTo expr

    override fun toString() = "$variable != $expr"
}

infix fun Char.notEqualsTo(expr: Expression) = NotEqualsTo(this, expr)
