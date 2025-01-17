package expressions.longs

import expressions.Expression

abstract class LongExpression(protected var _body: MutableList<Expression>) : Expression() {
    override val body: List<Expression>
        get() = _body

    override fun simplifyBody() {
        _body = _body.map { it.simplified() }
            .toMutableList()
    }
}