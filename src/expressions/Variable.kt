package expressions

import expressions.longs.Product
import expressions.numerical.Rational

class Variable(override val body: Char) : Expression(true) {
    override fun simplify(): Expression = this
    override fun simplifySoftly(): Variable = this

    override fun commonFactor(other: Expression): Variable? {
        return if (other == this)                      this
        else                                           null
    }

    override fun reduceOrNull(other: Expression): Expression? {
        if (!other.final) TODO()
        return when(other) {
            is Rational -> (this / other).simplify()
            is Variable -> if (other == this) unit() else null
            else -> null
        }
    }

    override fun toString(): String = body.toString()
}