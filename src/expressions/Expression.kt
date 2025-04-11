package expressions

import expressions.binary.Quotient
import expressions.longs.Product
import expressions.longs.Sum
import expressions.numerical.Rational

abstract class Expression (open val final: Boolean = false) {
    abstract val body: Any

    abstract fun simplify(): Expression
    abstract fun simplifySoftly(): Expression

    internal open fun commonFactor(other: Expression): Expression? = null
    open fun reduceOrNull(other: Expression): Expression? = null

    fun isUnitFraction(): Boolean = this is Rational && this.isUnit()
    fun isNullFraction(): Boolean = this is Rational && this.isNull()

    override fun equals(other: Any?): Boolean {
        if (other == null) return false
        if (other::class == this::class) {
            other as Expression
            return other.body == this.body
        }
        return false
    }

    abstract operator fun unaryMinus(): Expression

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

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }
}