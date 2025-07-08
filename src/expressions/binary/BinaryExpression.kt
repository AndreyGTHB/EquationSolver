package expressions.binary

import console.Clr
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
            if (it.first == InvalidExpression || it.second == InvalidExpression) this.makeInvalid()
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

    override fun toString(): String {
        var thisString = "${this::class.simpleName}:("
        body.toList().forEach { thisString += "$it " }
        thisString = thisString.slice(0 until thisString.lastIndex) + ')'
        return thisString
    }
    override fun toColouredString(): String {
        var thisString = "${this::class.simpleName}:".coloured()
        body.toList().forEach { subExpr ->
            val subExprString = subExpr.toColouredString()
                .split("\n")
                .joinToString("\n") { "  $it" }
                .run { "\u00b7".coloured() + slice(1 .. lastIndex) }
            thisString += "\n" + subExprString
        }
        return thisString
    }

    private fun String.coloured() = Clr.fg(Clr.palette[3]) + this + Clr.RC
}