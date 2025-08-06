package equations

import expressions.*
import expressions.binary.Power
import expressions.binary.Quotient
import expressions.longs.Sum
import expressions.monomials.raisedTo
import expressions.number.Rational
import expressions.number.over
import rules.*
import rules.statements.equalsTo
import utils.fold
import utils.replaceAllIndexed

class Equation (
    val body: ExpressionPair,
    val considerDomain: Boolean = true,
    val aim: Char = body.firstVariable() ?: 'x', // NOPT
) {
    fun solve(): Rule = _solve().run { if (considerDomain) (first * second).simplify() else first.simplify() }
    private fun _solve(): Pair<Rule, Rule> {
        val domain: Rule
        val currLeft: Expression
        body.simplify().apply {
            domain = loadDomain()
            currLeft = moveAllToTheLeft().removeQuotients()
        }

        val coefficientsMap = currLeft.calculateCoefficients(aim)
        val solution = when (currLeft.degree(aim)) { // NOPT (double degree calculation)
            zero() -> coefficientsMap.solveAsConstantPolynomial() // NOPT (rational keys reinitialisation)
            one()  -> coefficientsMap.solveAsLinearPolynomial()
            two()  -> coefficientsMap.solveAsQuadraticPolynomial() // coefficientsMap.solveAsQuadraticPolynomial(aimChar)
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
            val coeff = it.reduce(aim raisedTo degree)
            coefficientsMap[degree] = (coefficientsMap[degree] ?: zero()) + coeff
        }
        return coefficientsMap.mapValues { (_, coeff) -> coeff.simplify() }
    }

    private fun Map<Rational, Expression>.solveAsConstantPolynomial(): Rule {
        val a = get(zero())!!
        if (a.isNumber) {
            return if (a.isZeroRational()) Tautology
                   else                    Contradiction
        }
        val subEquation = Equation(a to zero(), false, a.firstVariable()!!)
        return subEquation.solve()
    }

    private fun Map<Rational, Expression>.solveAsLinearPolynomial(): Rule {
        val a = get(one())!!
        val b = get(zero()) ?: zero()
        val linearSolution = run {
            val aimCondition = aim equalsTo (-b) / a
            val aCondition = -Equation(a to zero(), false).solve()
            aimCondition * aCondition
        }
        val constantSolution = run {
            val aCondition = Equation(a to zero(), false).solve()
            val bCondition = Equation(b to zero(), false).solve()
            aCondition * bCondition
        }
        return linearSolution + constantSolution
    }

    private fun Map<Rational, Expression>.solveAsQuadraticPolynomial(): Rule {
        val a = get(two())!!
        val b = get(one()) ?: zero()
        val c = get(zero()) ?: zero()

        val aEqualsToZero = Equation(a to zero(), false).solve()
        val quadraticSolution = run {
            val aCondition = -aEqualsToZero
            val d = (b.raisedTo(two()) - four() * a * c).simplify()
            val dCondition = TextRule("$d >= 0") // ToDo: implement inequality
            val aimCondition1 = aim equalsTo (-b + d.raisedTo(1 over 2)) / (two() * a)
            val aimCondition2 = aim equalsTo (-b - d.raisedTo(1 over 2)) / (two() * a)
            (aimCondition1 + aimCondition2) * aCondition * dCondition
        }
        val linearSolution = run {
            val aCondition = aEqualsToZero
            mapOf(one() to b, zero() to c).solveAsLinearPolynomial() * aCondition
        }
        return quadraticSolution + linearSolution
    }
}
