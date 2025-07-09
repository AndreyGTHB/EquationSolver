package expressions

import console.Clr
import console.coloured

object InvalidExpression : Expression(final = true) {
    override val body = null

    override fun _simplify() = this
    override fun toString() = "IE"
    override fun coloured() = "IE".coloured(Clr.INVALID_EXPR)
}