package statements

import console.Clr
import console.coloured
import expressions.Expression

class EqualsTo(variable: Char, expr: Expression) : SimpleStatement(variable to expr) {
    override fun simplify() = EqualsTo(variable, expr.simplify())
    override fun unaryMinus() = variable notEqualsTo expr

    override fun toString() = "$variable = $expr"
    override fun coloured() = toString().coloured(Clr.EQUALITY)
}

infix fun Char.equalsTo(expr: Expression) = EqualsTo(this, expr)
