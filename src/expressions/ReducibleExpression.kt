package expressions

import utils.toFraction

abstract class ReducibleExpression (final: Boolean) : Expression(final) {
    abstract fun reduceOrNull(other: Expression): Expression?

    abstract fun commonFactor(other: ReducibleExpression): Expression
    fun commonFactor(other: Expression): Expression {
        return if (other !is ReducibleExpression) 1.toFraction() else commonFactor(other)
    }
}