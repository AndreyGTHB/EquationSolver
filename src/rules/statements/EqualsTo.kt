package rules.statements

import console.Clr
import console.coloured
import expressions.Expression
import expressions.InvalidExpression
import rules.Complement
import rules.Conjunction
import rules.Contradiction
import rules.Rule

class EqualsTo(body: Pair<Char, Expression>) : Statement(body) {
    val expr = body.second

    override fun _simplify(): Rule {
        return when (val it = expr.simplify()) {
            is InvalidExpression   -> Contradiction
            else                   -> EqualsTo(variable to it)
        }
    }

    override fun _contradictsStatement(other: Statement): Boolean? {
        return when (other) {
            is EqualsTo -> this.expr.isNumber && other.expr.isNumber && this.expr != other.expr
            else        -> null
        }
    }

    override fun toString() = "$variable = $expr"
    override fun coloured() = toString().coloured(Clr.EQUALITY)
}

infix fun Char.equalsTo(expr: Expression) = EqualsTo(this to expr)
infix fun Char.notEqualsTo(expr: Expression) = Complement(EqualsTo(this to expr))
