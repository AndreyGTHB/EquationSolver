package equations

import expressions.Expression

class Equation (body: Pair<Expression, Expression>) {
    var body = body
        private set
    var left
        get() = body.first
        private set(value) { body = value to right }
    var right
        get() = body.second
        private set(value) { body = left to value }

    operator fun plus(other: Expression): Equation = left + other equateTo right + other

    operator fun minus(other: Expression): Equation = left - other equateTo right - other

    operator fun times(other: Expression): Equation = left * other equateTo right * other

    operator fun div(other: Expression): Equation = left / other equateTo right / other
}


infix fun Expression.equateTo(other: Expression) = Equation(this to other)
