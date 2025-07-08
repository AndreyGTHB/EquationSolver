package expressions

import console.Clr

object UniversalExpression : Expression(final=true) {
    override val body = null

    override fun _simplify() = this
    override fun toString() = "UE"
    override fun toColouredString() = Clr.ORANGE + "UE" + Clr.RC
}