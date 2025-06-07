package expressions.longs

import expressions.Expression

abstract class LongExpression (override val body: List<Expression>, final: Boolean) : Expression(final) {
    override fun _isNumber() = body.all { it.isNumber() }

    protected fun simplifyBody(): List<Expression> {
        val newBody = body.map { it.simplify() } .toList()
        return newBody
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
