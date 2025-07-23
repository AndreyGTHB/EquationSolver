package rules

import rules.statements.Statement

class Conjunction (body: Set<Rule>) : LongRule(body) {
    constructor(vararg body: Rule) : this(body.toSet())

    override fun _simplify(): Rule {
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

    override fun _contradicts(other: Rule): Boolean? = when (other) {
        is Statement   -> contradictsStatement(other)
        is Conjunction -> contradictsConjunction(other)
        is Disjunction -> contradictsDisjunction(other)
        else           -> null
    }

    private fun contradictsStatement(other: Statement) = body.any { it contradicts other }

    private fun contradictsConjunction(other: Conjunction): Boolean {
        return this.body.any { subRule1 -> other.body.any { subRule2 -> subRule1 contradicts subRule2 } }
    }

    private fun contradictsDisjunction(other: Disjunction): Boolean {
        return this.body.any { subRule1 -> other.body.all { subRule2 -> subRule1 contradicts subRule2 } }
    }
}