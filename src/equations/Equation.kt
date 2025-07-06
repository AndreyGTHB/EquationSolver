package equations

import expressions.*
import expressions.binary.Quotient
import expressions.longs.Sum
import expressions.monomials.Monomial
import statements.*

class Equation (left: Expression, right: Expression, val aimChar: Char = 'x') {
    var left = left
        private set
    var right = right
        private set

    val aimMonomial = Monomial(mapOf(aimChar to unit()), final=true)

    val domain: StatementSet
        get() = left.domain * right.domain

    fun solve(): Solution {
        moveAllToTheLeft()
        multiplyByDenoms()
        separateByAim()

        if (left.isZeroRational()) {
            return if (right.isZeroRational()) Solution(aimChar equalsTo UniversalExpression, UniversalSet)
            else                               Solution(aimChar equalsTo InvalidExpression, UniversalSet)
        }

        expressX()
        val answer = EqualsTo(aimChar, right)
        return Solution(answer, domain)
    }

    private fun moveAllToTheLeft() {
        left -= right
        right = zero()
        simplifyBody()
    }

    private fun multiplyByDenoms() {
        val leftSum = left.asSum()
        leftSum.body.forEach { if (it is Quotient) left *= it.denom }
        simplifyBody()
    }

    private fun separateByAim() {
        val leftSum = left.asSum()
        left = Sum(domain=left.domain)
        right = Sum(domain=right.domain)
        leftSum.body.forEach {
            if (it.reduceOrNull(aimMonomial) != null) left += it
            else                                      right -= it
        }
        simplifyBody()
    }

    private fun expressX() {
        val xCoeff = left.reduce(aimMonomial)
        left = aimMonomial
        right /= xCoeff
        simplifyBody()
    }

    private fun simplifyBody() {
        left = left.simplify()
        right = right.simplify()
    }
}
