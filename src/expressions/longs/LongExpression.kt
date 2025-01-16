package expressions.longs

import expressions.Expression

abstract class LongExpression(override val body: List<Expression>) : Expression() {
    protected open fun simplifyBody() {
        body.forEach { it.simplify() }
    }
}