package expressions

import console.Clr
import console.coloured

object InvalidExpression : Expression(final = true) {
    override val body = null

    override fun _simplify() = this
    override fun _commonFactor(other: Expression) = null
    override fun _reduceOrNull(other: Expression) = null

    override fun toString() = "IE"
    override fun coloured() = "IE".coloured(Clr.INVALID_EXPR)
}