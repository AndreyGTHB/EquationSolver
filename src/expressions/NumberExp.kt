package expressions

class NumberExp(override var body: Int) : Expression() {
    override fun simplify(): NumberExp { return this }
}