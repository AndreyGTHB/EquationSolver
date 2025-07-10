package expressions.longs

import console.Clr
import console.colouredUnder
import console.toStringUnder
import expressions.CompoundExpression
import expressions.Expression
import expressions.InvalidExpression
import expressions.monomials.Monomial
import rules.Rule
import rules.Tautology

abstract class LongExpression (
    override val body: List<Expression>,
    domain: Rule = Tautology,
    final: Boolean = false
) : CompoundExpression(domain, final) {
    override val isNumber by lazy { body.all { it.isNumber } }

    protected fun simplifyBody(): List<Expression> {
        val newBody = body.map { subExpr ->
            subExpr.simplify().also {
                if (it == InvalidExpression) this.makeInvalid()
                bodyDomain *= it.domain
            }
        }.toList()
        return newBody
    }

    protected fun emptyBody() = mutableListOf<Expression>()

    override fun firstVariable(): Char? {
        body.forEach { subExpr ->
            when (subExpr) {
                is Monomial ->           subExpr.body.keys.firstOrNull().let { if (it != null) return it }
                is CompoundExpression -> subExpr.firstVariable().let { if (it != null) return it }
            }
        }
        return null
    }

    override fun contains(variable: Char) = body.any { it.contains(variable) }
    protected fun substituteIntoBody(variable: Char, value: Expression) = body.map { it.substitute(variable, value) }

    override fun toString() = body.toStringUnder(this::class.simpleName!!)
    override fun coloured() = body.colouredUnder(this::class.simpleName!!, Clr.LONG_EXPR)
}
