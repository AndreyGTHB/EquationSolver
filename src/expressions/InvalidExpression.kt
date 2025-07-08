package expressions

import console.Clr

object InvalidExpression : Expression(final = true) {
    override val body = null

    override fun _simplify() = this
    override fun toString() = "IE"
    override fun toColouredString() = Clr.ORANGE + "IE" + Clr.RC
}