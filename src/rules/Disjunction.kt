package rules

import rules.statements.Statement

class Disjunction(body: Set<Rule>) : LongRule(body) {
    constructor(vararg body: Rule) : this(body.toSet())

    override fun _simplify(): Rule {
        val newBody = simplifyBody()
            .expandDisjunctions()
            .checkForTautologiesAndContradictions()
            .toSortedSet()
        return when (newBody.size) {
            0    -> Contradiction
            1    -> newBody.first()
            else -> Disjunction(newBody)
        }
    }

    private fun Set<Rule>.expandDisjunctions(): Set<Rule> {
        val newBody = emptyBody()
        forEach {
            if (it is Disjunction) newBody.addAll(it.body)
            else                   newBody.add(it)
        }
        return newBody
    }

    private fun Set<Rule>.checkForTautologiesAndContradictions(): Set<Rule> {
        val newBody = emptyBody()
        forEach {
            when (it) {
                is Contradiction -> return@forEach
                is Tautology     -> return setOf(Tautology)
            }
            newBody.add(it)
        }
        return newBody
    }

    override fun _union(other: Rule): Rule {
        return if (other is Disjunction) Disjunction(this.body + other.body)
               else                      Disjunction(this.body + other)
    }

    override fun unaryMinus() = Conjunction(body.map { -it }.toSet())

    override fun _contradicts(other: Rule): Boolean? = when (other) {
        is Statement   -> contradictsStatement(other)
        is Disjunction -> contradictsDisjunction(other)
        else           -> null
    }

    private fun contradictsStatement(other: Statement) = body.all { it contradicts other }

    private fun contradictsDisjunction(other: Disjunction): Boolean {
        return this.body.all { subRule1 -> other.body.all { subRule2 -> subRule1 contradicts subRule2 } }
    }
}