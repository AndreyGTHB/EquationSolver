package statements

import console.Colourable

abstract class Statement : Comparable<Statement>, Colourable {
    abstract val body: Any?

    abstract fun simplify(): Statement

    protected abstract fun _intersect(other: Statement): Statement?
    operator fun times(other: Statement): Statement {
        return this._intersect(other) ?: other._intersect(this) ?: Contradiction
    }

    override fun hashCode() = body.hashCode()
    override fun equals(other: Any?): Boolean {
        if (other == null) return false
        if (other::class != this::class) return false
        other as Statement
        return other.body == this.body
    }

    override fun compareTo(other: Statement) = this.toString() compareTo other.toString()

    abstract operator fun unaryMinus(): Statement

    abstract override fun toString(): String
}