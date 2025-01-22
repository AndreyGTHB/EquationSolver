package expressions

import expressions.binary.Quotient
import expressions.longs.Product
import expressions.longs.Sum

abstract class Expression {
    abstract val body: Any

    abstract fun simplified(): Expression
    abstract fun simplifiedSoftly(): Expression

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
}