package expressions

import expressions.binary.Quotient
import expressions.longs.Product
import expressions.longs.Sum
import expressions.number.Rational

abstract class Expression (open val final: Boolean = false) : Comparable<Expression> {
    abstract val body: Any

    abstract fun simplify(): Expression

    internal open fun commonFactor(other: Expression): Expression? = null
    protected open fun _reduceOrNull(other: Expression): Expression? = null
    fun reduceOrNull(other: Expression): Expression? {
        if (!(this.final && other.final)) TODO("Reducing non-simplified expressions")
        if (other.isZeroRational()) TODO("Reducing by zero")
        if (other.isUnitRational() || this.isZeroRational()) return this
        return _reduceOrNull(other)?.simplify()
    }
    fun reduce(other: Expression): Expression = reduceOrNull(other)!!

    fun isUnitRational(): Boolean = this is Rational && this.isUnit()
    fun isZeroRational(): Boolean = this is Rational && this.isNull()

    override fun equals(other: Any?): Boolean {
        if (other == null) return false
        if (other::class != this::class) return false
        other as Expression
        return this.toString() == other.toString()
    }

    override fun compareTo(other: Expression): Int {
        return this.toString() compareTo other.toString()
    }

    open operator fun unaryMinus(): Expression = (-unit()) * this

    open operator fun plus(other: Expression): Sum {
        return Sum(listOf(this, other))
    }
    open operator fun minus(other: Expression): Sum {
        return Sum(listOf(this, -other))
    }
    open operator fun times(other: Expression): Product {
        return Product(listOf(this, other))
    }
    open operator fun div(other: Expression): Quotient {
        return Quotient(this to other)
    }

    abstract override fun toString(): String
    override fun hashCode(): Int = body.hashCode()
}