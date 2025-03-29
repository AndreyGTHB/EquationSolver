package expressions

import utils.toFraction

abstract class ReducibleExpression (final: Boolean) : Expression(final) {
    abstract fun reduceOrNull(other: Expression): Expression?

    abstract fun commonFactor(other: Expression): Expression
}