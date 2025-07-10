package equations

import expressions.*
import expressions.binary.Quotient
import expressions.longs.Sum
import expressions.monomials.Monomial
import rules.*
import rules.statements.EqualsTo
import rules.statements.equalsTo
import utils.fold
import utils.replaceAllIndexed

class Equation (left: Expression, right: Expression, val aimChar: Char = 'x') {
    var left = left
        private set
    var right = right
        private set

    val aimMonomial = Monomial(mapOf(aimChar to unit()), final=true)

    private var domain: Rule = Tautology

    fun solve(): Solution {
        loadDomain()
        moveAllToTheLeft()
        removeQuotients()
        separateByAim()

        if (left.isZeroRational()) {
            return if (right.isZeroRational()) Solution(aimChar equalsTo UniversalExpression, Tautology)
            else                               Solution(aimChar equalsTo InvalidExpression, Tautology)
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
            val leftSumBody = left.asSum().body.toMutableList()
            val denomsMap = mutableMapOf<Int, Expression>()
            leftSumBody.replaceAllIndexed { i, term ->
                if (term is Quotient) {
                    thereAreQuotients = true
                    denomsMap[i] = term.denom
                    term.numer
                }
                else term
            }
            left = leftSumBody.mapIndexed { i, term ->
                if (i in denomsMap) denomsMap.fold(term as Expression) { acc, (j, denom) ->
                    if (i != j) acc * denom else acc
                }
                else                denomsMap.fold(term) { acc, (_, denom) -> acc * denom }
            }.let { Sum(it) }
            left = left.simplify()
        }
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
