package statements

import expressions.Expression

abstract class Statement (val variable: Char, val expr: Expression) : StatementSet() {
    override fun _intersect(other: StatementSet): Conjunction? {
        val otherAsStatement = (other as? Statement) ?: return null
        return Conjunction(this, otherAsStatement)
    }

    override fun hashCode() = (variable to expr).hashCode()
    override fun equals(other: Any?): Boolean {
        if (other == null) return false
        if (this::class != other::class) return false
        other as Statement

        return this.variable == other.variable
            && this.expr == other.expr
    }
}