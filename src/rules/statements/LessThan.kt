package rules.statements

import console.Clr
import console.coloured
import expressions.Expression
import expressions.InvalidExpression
import rules.Conjunction
import rules.Contradiction
import rules.Disjunction
import rules.Rule

class LessThan (body: Pair<Char, Expression>) : Statement(body) {
    val expr = body.second

    override fun _simplify(): Rule {
        return when (val sExpr = expr.simplify()) {
            is InvalidExpression -> Contradiction
            else                 -> LessThan(variable to sExpr)
        }
    }

    override fun contradictsStatement(other: Statement): Boolean? {
        if (!this.expr.isNumber) return false
        return when (other) {
            is EqualsTo    -> other.expr.isNumber && (this.expr == other.expr || this.expr lessThan other.expr)
            is GreaterThan -> other.expr.isNumber && (this.expr == other.expr || this.expr lessThan other.expr)
            else           -> null
        }
    }

    override fun _implies(other: Rule): Boolean {
        if (!this.expr.isNumber) return false
        return when (other) {
            is LessThan    -> other.expr.isNumber && this.expr lessThan other.expr
            is Conjunction -> other.body.all { _implies(it) }
            is Disjunction -> other.body.any { _implies(it) }
            else           -> false
        }
    }

    override fun toString() = "$variable < $expr"
    override fun coloured() = "$variable < ".coloured(Clr.INEQUALITY) + expr.coloured()
}

infix fun Char.lessThan(expr: Expression) = LessThan(this to expr)
infix fun Char.lessThanUnstrictly(expr: Expression) = Disjunction(equalsTo(expr), lessThan(expr))
