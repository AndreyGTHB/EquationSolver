package expressions.binary

import equations.Domain
import equations.FullDomain
import expressions.Expression
import expressions.InvalidExpression

abstract class BinaryExpression (
    override val body: Pair<Expression, Expression>,
    domain: Domain = FullDomain,
    final: Boolean
) : Expression(domain, final) {
    private lateinit var bodyDomain: Domain

    override fun _isNumber() = body.first.isNumber() && body.second.isNumber()

    protected fun simplifyBody(): Pair<Expression, Expression> {
        return (body.first.simplify() to body.second.simplify()).also {
            if (it.first == InvalidExpression || it.second == InvalidExpression) this.makeInvalid()
            bodyDomain = it.first.domain * it.second.domain
        }
    }

    override fun _fullDomain() = bodyDomain * domain

    override fun toString(): String {
        var asString = "${this::class.simpleName}:\n"
        arrayOf(body.first, body.second).forEach { subExp ->
            val subExpStrs = subExp.toString().split("\n")
            subExpStrs.forEach { subExpStr -> asString += "  $subExpStr\n" }
        }
        return asString
    }
}