package statements

import expressions.Expression

abstract class SimpleStatement (final override val body: Pair<Char, Expression>) : Statement() {
    val variable = body.first
    val expr = body.second

    override fun _intersect(other: Statement): Conjunction? {
        val otherAsSimpleStatement = (other as? SimpleStatement) ?: return null
        return Conjunction(this, otherAsSimpleStatement)
    }
}