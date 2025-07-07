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

    private var domain: StatementSet = UniversalSet

    fun solve(): Solution {
        loadDomain()
        moveAllToTheLeft()
        removeQuotients()
        separateByAim()

        if (left.isZeroRational()) {
            return if (right.isZeroRational()) Solution(aimChar equalsTo UniversalExpression, UniversalSet)
            else                               Solution(aimChar equalsTo InvalidExpression, UniversalSet)
        }

        expressX()
        val answer = EqualsTo(aimChar, right)
        return Solution(answer, domain)
    }

    private fun loadDomain() {
        simplifyBody()
        domain = left.domain * right.domain
    }

    private fun moveAllToTheLeft() {
        left -= right
        right = zero()
        simplifyBody()
    }

    private fun removeQuotients() {
        var thereAreQuotients = true
        while (thereAreQuotients) {
            thereAreQuotients = false
            val leftAsSum = left.asSum()
            leftAsSum.body.forEach { if (it is Quotient) {
                left *= it.denom
                thereAreQuotients = true
            }}
            left = left.simplify()
        }
        simplifyBody()
    }

    private fun separateByAim() {
        val leftSum = left.asSum()
        left = Sum()
        right = Sum()
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
