package statements

import console.Clr
import console.coloured
import expressions.Expression

class NotEqualsTo (variable: Char, expr: Expression) : Statement(variable to expr) {
    override fun simplify() = NotEqualsTo(variable, expr.simplify())
    override fun unaryMinus() = variable equalsTo expr

    override fun toString() = "$variable != $expr"
    override fun coloured() = toString().coloured(Clr.EQUALITY)
}

infix fun Char.notEqualsTo(expr: Expression) = NotEqualsTo(this, expr)
