package expressions.longs

import console.Clr
import expressions.CompoundExpression
import expressions.Expression
import expressions.InvalidExpression
import expressions.monomials.Monomial
import statements.StatementSet
import statements.UniversalSet

abstract class LongExpression (
    override val body: List<Expression>,
    domain: StatementSet = UniversalSet,
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

    protected fun substituteIntoBody(variable: Char, value: Expression) = body.map {
        if (variable in it) it.substitute(variable, value) else it
    }

    override fun toString(): String {
        var thisString = "${this::class.simpleName}:".coloured()
        body.forEach { subExpr ->
            val subExprString = subExpr.toString()
                .split("\n")
                .joinToString("\n") { "  $it" }
                .run { "\u00b7".coloured() + slice(1 .. lastIndex) }
            thisString += "\n" + subExprString
        }
        return thisString
    }

    private fun String.coloured() = Clr.fg(Clr.palette[2]) + this + Clr.RC
}
