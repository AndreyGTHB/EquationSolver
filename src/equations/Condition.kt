package equations

import expressions.Expression

abstract class Condition(val body: Pair<Expression, Expression>, final: Boolean = false) : Domain(final), Comparable<Condition> {
    val left
        get() = body.first
    val right
        get() = body.second

    override fun times(other: Domain): Domain {
        return when (other) {
            is EmptyDomain -> other
            is FullDomain ->  this
            else ->           PermittingDomain(this) * other
        }
    }

    override fun compareTo(other: Condition) = this.toString() compareTo other.toString()

//    operator fun plus(other: Expression) = left + other equateTo right + other
//    operator fun plusAssign(other: Expression) { body = left + other to right + other }
//
//    operator fun minus(other: Expression) = left - other equateTo right - other
//    operator fun minusAssign(other: Expression) { body = left - other to right - other }
//
//    operator fun times(other: Expression) = left * other equateTo right * other
//    operator fun timesAssign(other: Expression) { body = left * other to right * other }
//
//    operator fun div(other: Expression) = left / other equateTo right / other
//    operator fun divAssign(other: Expression) { body = left / other to right / other }

    override fun hashCode() = body.hashCode()
    override fun equals(other: Any?): Boolean {
        if (other == null) return false
        if (other::class != this::class) return false
        other as Condition
        return other.body == this.body
    }

    override fun toString() = body.toString()
}
