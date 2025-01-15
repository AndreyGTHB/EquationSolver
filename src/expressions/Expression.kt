package expressions

import expressions.binary.*

abstract class Expression {
    abstract val body: Any

    abstract fun simplify(): Expression

    operator fun plus(exp: Expression): Sum {
        return Sum(this to exp)
    }
    operator fun minus(exp: Expression): Difference {
        return Difference(this to exp)
    }
    operator fun times(exp: Expression): Product {
        return Product(this to exp)
    }
    operator fun div(exp: Expression): Fraction {
        return Fraction(this to exp)
    }
}