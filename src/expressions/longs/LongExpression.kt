package expressions.longs

import expressions.CompoundExpression
import expressions.Expression
import expressions.InvalidExpression
import expressions.monomials.Monomial
import statements.Conjunction
import statements.Statement
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

    override fun toString(): String {
        var asString = "${this::class.simpleName}:\n"
        body.forEach { subExp ->
            val subExpStrs = subExp.toString().split("\n")
            subExpStrs.forEach { subExpStr -> asString += "  $subExpStr\n" }
        }
        return asString
    }
}
