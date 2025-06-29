package expressions.longs

import equations.Domain
import equations.FullDomain
import expressions.Expression
import expressions.InvalidExpression

abstract class LongExpression (
    override val body: List<Expression>,
    domain: Domain = FullDomain,
    final: Boolean = false
) : Expression(domain, final) {
    private var bodyDomain: Domain = FullDomain

    override fun _isNumber() = body.all { it.isNumber() }

    protected fun simplifyBody(): List<Expression> {
        val newBody = body.map { subExpr ->
            subExpr.simplify().also {
                if (it == InvalidExpression) this.makeInvalid()
                bodyDomain *= it.domain
            }
        }.toList()
        return newBody
    }

    override fun _fullDomain() = bodyDomain * domain

    override fun toString(): String {
        var asString = "${this::class.simpleName}:\n"
        body.forEach { subExp ->
            val subExpStrs = subExp.toString().split("\n")
            subExpStrs.forEach { subExpStr -> asString += "  $subExpStr\n" }
        }
        return asString
    }
}
