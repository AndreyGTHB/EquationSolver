package rules

import rules.statements.Statement

class Conjunction (body: Set<Rule>) : LongRule(body) {
    constructor(vararg body: Rule) : this(body.toSet())

    override fun simplify(): Rule {
        val newBody = simplifyBody()
            .expandConjunctions()
            .checkForTautologiesAndContradictions()
            .toSortedSet()
        return when (newBody.size) {
            0    -> Tautology
            1    -> newBody.first()
            else -> Conjunction(newBody)
        }
    }

    private fun Set<Rule>.expandConjunctions(): Set<Rule> {
        val newBody = emptyBody()
        forEach {
            if (it is Conjunction) newBody.addAll(it.body)
            else                   newBody.add(it)
        }
        return newBody
    }

    private fun Set<Rule>.checkForTautologiesAndContradictions(): Set<Rule> {
        val newBody = emptyBody()
        val statementMap = mutableMapOf<Char, MutableList<Statement>>()
        forEach {
            when (it) {
                is Tautology     -> return@forEach
                is Contradiction -> return setOf(Contradiction)
                is Statement     -> {
                    if (statementMap[it.variable] == null) statementMap[it.variable] = mutableListOf()
                    statementMap[it.variable]!!.add(it)
                }
            }
            newBody.add(it)
        }
        statementMap.forEach { (_, statements) ->
            for ((i, st1) in statements.withIndex()) {
                for (st2 in statements.slice(i+1 ..< statements.size)) if (st1 contradicts st2) return setOf(Contradiction)
            }
        }
        return newBody
    }

    override fun _intersect(other: Rule) = if (other is Conjunction) Conjunction(this.body.union(other.body))
                                           else                      Conjunction(this.body + other)

    override fun unaryMinus() = Disjunction(body.map { -it }.toSet())
}