package expressions.numerical

import expressions.Expression

class Integer(override val body: Int) : Expression() {
    override fun simplify() { }
}