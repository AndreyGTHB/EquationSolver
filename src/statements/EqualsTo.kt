package statements

import expressions.Expression

class EqualsTo(variable: Char, expr: Expression) : Statement(variable, expr) {
    override fun simplify() = EqualsTo(variable, expr.simplify())
    override fun unaryMinus() = variable notEqualsTo expr

    override fun toString() = "$variable = $expr"
}

infix fun Char.equalsTo(expr: Expression) = EqualsTo(this, expr)
