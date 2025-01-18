package expressions.longs

import expressions.Expression

abstract class LongExpression(body: Collection<Expression>) : Expression() {
    protected var _body = body.toMutableList()
    override val body: List<Expression>
        get() = _body

    override fun simplifyBody() {
        _body = _body.map { it.simplified() }
            .toMutableList()
    }
}