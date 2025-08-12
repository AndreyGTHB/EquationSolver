package rules

import console.Clr
import console.coloured
import rules.statements.GreaterThan
import rules.statements.LessThan
import rules.statements.equalsTo
import rules.statements.greaterThan
import rules.statements.lessThan

class Complement (override val body: Rule) : Rule() {
    override fun _simplify() = when (val sBody = body.simplify()) {
        is Contradiction -> Tautology
        is Tautology     -> Contradiction
        is Complement    -> sBody.body
        is Disjunction   -> Conjunction(sBody.body.map { -it }.toSet()).simplify()
        is Conjunction   -> Disjunction(sBody.body.map { -it }.toSet()).simplify()
        is LessThan      -> sBody.run { // ToDo: IE
            Disjunction(variable.equalsTo(expr), variable.greaterThan(expr)).simplifyNotAnalysing().finalOn()
        }
        is GreaterThan   -> sBody.run {
            Disjunction(variable.equalsTo(expr), variable.lessThan(expr)).simplifyNotAnalysing().finalOn()
        }
        else -> Complement(sBody)
    }

    override fun unaryMinus() = body

    override fun _contradicts(other: Rule) = (other == body).takeIf { it }

    override fun toString() = "!($body)"

    override fun coloured() = "! ".coloured(Clr.COMPLEMENT) + body.coloured()
}