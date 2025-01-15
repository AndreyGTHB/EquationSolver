package expressions.binary

import expressions.Expression

abstract class BinaryExpression (override var body: Pair<Expression, Expression>) : Expression() {
    protected open fun simplifyBody() {
        body = body.first.simplify() to body.second.simplify()
    }
}