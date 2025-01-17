package expressions.binary

import expressions.Expression

abstract class BinaryExpression (protected var _body: Pair<Expression, Expression>) : Expression() {
    override val body
        get() = _body

    override fun simplifyBody() {
        _body = _body.first.simplified() to _body.second.simplified()
    }
}