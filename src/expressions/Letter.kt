package expressions

class Letter(override var body: Char) : Expression() {
    override fun simplify(): Letter { return this }
}