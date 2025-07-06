package expressions

object UniversalExpression : Expression(final=true) {
    override val body = null

    override fun _simplify() = this
    override fun toString() = "UE"
}