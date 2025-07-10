package statements

import expressions.Expression

abstract class Statement (final override val body: Pair<Char, Expression>) : Rule() {
    val variable = body.first
    val expr = body.second
}