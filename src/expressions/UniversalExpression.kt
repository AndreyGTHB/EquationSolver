package expressions

import console.Clr
import console.coloured

object UniversalExpression : Expression(final=true) {
    override val body = null

    override fun _simplify() = this
    override fun _commonFactor(other: Expression) = null
    override fun _reduceOrNull(other: Expression) = null

    override fun toString() = "UE"
    override fun coloured() = "UE".coloured(Clr.UNIVERSAL_EXPR)
}