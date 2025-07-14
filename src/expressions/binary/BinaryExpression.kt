package expressions.binary

import console.Clr
import console.colouredUnder
import console.toStringUnder
import expressions.CompoundExpression
import expressions.Expression
import expressions.InvalidExpression
import expressions.longs.Sum

abstract class BinaryExpression (
    override val body: Pair<Expression, Expression>,
    final: Boolean
) : CompoundExpression(final=final) {
    override val isNumber by lazy { body.run { first.isNumber && second.isNumber } }

    protected fun simplifyBody(): Pair<Expression, Expression> {
        return (body.first.simplify() to body.second.simplify()).also {
            if (it.first == InvalidExpression || it.second == InvalidExpression) makeInvalid()
            bodyDomain = it.first.domain * it.second.domain
        }
    }

    override fun firstVariable(): Char? {
        val asSum = Sum(body.toList())
        return asSum.firstVariable()
    }

    override fun contains(variable: Char) = body.first.contains(variable) || body.second.contains(variable)

    protected fun substituteIntoBody(variable: Char, value: Expression): Pair<Expression, Expression> {
        return body.first.substitute(variable, value) to body.second.substitute(variable, value)
    }

    override fun toString() = body.toList().toStringUnder(this::class.simpleName!!)
    override fun coloured() = body.toList().colouredUnder(this::class.simpleName!!, Clr.BIN_EXPR)
}