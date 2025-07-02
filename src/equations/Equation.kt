package equations

import expressions.*
import expressions.binary.Quotient
import expressions.longs.Product
import expressions.longs.Sum
import expressions.monomials.Monomial

class Equation (body: Pair<Expression, Expression>, final: Boolean = false) : Condition(body, final) {
    private val xMonomial = Monomial('x' to unit()).simplify()

    override fun _solve(): Domain {
        var newBody = body
            .moveAllToTheLeft()
            .multiplyByDenoms()
            .separateByX()

        if (newBody.first.isZeroRational()) {
            return if (newBody.second.isZeroRational()) newBody.domain()
            else                                        EmptyDomain
        }

        newBody = newBody.expressX()
        val sThis = Equation(newBody, true)
        val result = sThis * newBody.domain()
        return result.solve()
    }

    private fun Pair<Expression, Expression>.moveAllToTheLeft(): Pair<Expression, Expression> {
        val newBody = (first - second) to zero()
        return newBody.simplify()
    }

    private fun Pair<Expression, Expression>.multiplyByDenoms(): Pair<Expression, Expression> {
        val leftSum = first.asSum()
        var newLeft = Product(first)
        leftSum.body.forEach { if (it is Quotient) newLeft *= it.denom }
        val newBody = newLeft to second
        return newBody.simplify()
    }

    private fun Pair<Expression, Expression>.separateByX(): Pair<Expression, Expression> {
        val leftSum = first.asSum()
        var newLeft = Sum()
        var newRight = Sum()
        leftSum.body.forEach {
            if (it.reduceOrNull(xMonomial) != null) newLeft += it
            else                                    newRight -= it
        }
        val newBody = newLeft to newRight
        return newBody.simplify()
    }

    private fun Pair<Expression, Expression>.expressX(): Pair<Expression, Expression> {
        val xCoeff = first.reduce(xMonomial)
        val newBody = xMonomial to (second / xCoeff)
        return newBody.simplify()
    }

    private fun Pair<Expression, Expression>.simplify() = first.simplify() to second.simplify()
    private fun Pair<Expression, Expression>.domain() = first.domain * second.domain
}


infix fun Expression.equateTo(other: Expression) = Equation(this to other)
