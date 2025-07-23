package equations

import expressions.*
import expressions.binary.Quotient
import expressions.longs.Sum
import expressions.monomials.Monomial
import expressions.number.Rational
import rules.*
import rules.statements.equalsTo
import utils.fold
import utils.replaceAllIndexed

class Equation (val body: ExpressionPair, val aimChar: Char = body.firstVariable() ?: 'x') { // NOPT
    val aimMonomial = Monomial(mapOf(aimChar to one()), final=true)

    private var domain: Rule = Tautology

    fun Char.raisedTo(exp: Rational): Expression = Monomial(this to exp).simplify()

    fun solve(): Rule = _solve().run { (first * second).simplify() }
    fun solveIgnoringDomain(): Rule = _solve().first.simplify()
    private fun _solve(): Pair<Rule, Rule> {
        val domain: Rule
        val currLeft: Expression
        body.simplify().apply {
            domain = loadDomain()
            currLeft = moveAllToTheLeft().removeQuotients()
        }

        val coefficientsMap = currLeft.calculateCoefficients(aimChar)
        val solution = when (currLeft.degree(aimChar)) { // NOPT (double degree calculation)
            zero() -> coefficientsMap.solveAsConstantPolynomial(aimChar) // NOPT (rational keys instead of ints)
            one()  -> coefficientsMap.solveAsLinearPolynomial(aimChar)
            two()  -> ExprEqualsTo(currLeft to zero()) // coefficientsMap.solveAsQuadraticPolynomial(aimChar)
            else   -> ExprEqualsTo(currLeft to zero())
        }
        return solution to domain
    }

    private fun ExpressionPair.loadDomain(): Rule = first.domain * second.domain

    private fun ExpressionPair.moveAllToTheLeft(): Expression = (first - second).simplify()

    private fun Expression.removeQuotients(): Expression {
        var thereAreQuotients = true
        var newThis = this
        while (thereAreQuotients) {
            thereAreQuotients = false
            val sumBody = newThis.asSum().body.toMutableList()
            val denomsMap = mutableMapOf<Int, Expression>()
            sumBody.replaceAllIndexed { i, term ->
                if (term is Quotient) {
                    thereAreQuotients = true
                    denomsMap[i] = term.denom
                    term.numer
                }
                else term
            }
            newThis = sumBody.mapIndexed { i, term ->
                if (i in denomsMap) denomsMap.fold(term) { acc, (j, denom) ->
                    if (i != j) acc * denom else acc
                }
                else                denomsMap.fold(term) { acc, (_, denom) -> acc * denom }
            }.let { Sum(it) }
            newThis = newThis.simplify()
        }
        return newThis
    }

    private fun Expression.calculateCoefficients(variable: Char): Map<Rational, Expression> {
        val coefficientsMap = mutableMapOf<Rational, Expression>()
        asSum().body.forEach {
            val degree = it.degree(variable) ?: zero()
            val coeff = it.reduce(aimChar.raisedTo(degree))
            coefficientsMap[degree] = (coefficientsMap[degree] ?: zero()) + coeff
        }
        return coefficientsMap.mapValues { (_, coeff) -> coeff.simplify() }
    }

    private fun Map<Rational, Expression>.solveAsConstantPolynomial(variable: Char): Rule {
        val a = get(zero())!!
        if (a.isNumber) {
            return if (a.isZeroRational()) Tautology
                   else                    Contradiction
        }
        val subEquation = Equation(a to zero(), a.firstVariable()!!)
        return subEquation.solve()
    }
    private fun Map<Rational, Expression>.solveAsLinearPolynomial(variable: Char): Rule {
        val a = get(one())!!
        val b = get(zero()) ?: zero()
        val firstSolution = run {
            val aimCondition = aimChar equalsTo (-b) / a
            val aCondition = -Equation(a to zero()).solve()
            aimCondition * aCondition
        }
        val secondSolution = run {
            val aCondition = Equation(a to zero()).solve()
            val bCondition = Equation(b to zero()).solve()
            aCondition * bCondition
        }
        return firstSolution + secondSolution
    }
    private fun Map<Rational, Expression>.solveAsQuadraticPolynomial(variable: Char): Rule = TODO()
}
