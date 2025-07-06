package expressions

object InvalidExpression : Expression(final = true) {
    override val body = null

    override fun _simplify() = this
    override fun toString() = "IE"
}