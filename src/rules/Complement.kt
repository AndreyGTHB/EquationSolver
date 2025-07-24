package rules

import console.Clr
import console.coloured

class Complement (override val body: Rule) : Rule() {
    override fun _simplify() = when (val sBody = body.simplify()) {
        is Contradiction -> Tautology
        is Tautology     -> Contradiction
        is Complement    -> sBody.body
        is Disjunction   -> Conjunction(sBody.body.map { -it }.toSet()).simplify()
        is Conjunction   -> Disjunction(sBody.body.map { -it }.toSet()).simplify()
        else             -> Complement(sBody)
    }

    override fun unaryMinus() = body

    override fun _contradicts(other: Rule) = (other == body).takeIf { it }

    override fun toString() = "!($body)"

    override fun coloured() = "! ".coloured(Clr.NOT) + body.coloured()
}