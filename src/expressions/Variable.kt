package expressions

class Variable(override var body: Char) : Expression() {
    override fun simplified(): Expression { return this }
    override fun simplifiedSoftly(): Variable { return this }
}