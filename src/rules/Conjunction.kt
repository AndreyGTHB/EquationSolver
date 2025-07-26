package rules

import utils.allExcept
import utils.forEachExcept

class Conjunction (body: Collection<Rule>) : LongRule(body.toSet()) {
    constructor(vararg body: Rule) : this(body.toSet())

    override fun _simplify(): Rule {
        val newBody = simplifyBody()
            .toList()
            .clean()
            .processPairs()
            .clean() // NOPT: double cleaning
            .toSortedSet()
        return when (newBody.size) {
            0    -> Tautology
            1    -> newBody.first()
            else -> Conjunction(newBody)
        }
    }

    private fun List<Rule>.clean() = this
        .flatMap { if (it is Conjunction) it.body else listOf(it) }
        .distinct()
        .filter {
            if (it is Contradiction) return listOf(Contradiction)
            it !is Tautology
        }

    private fun List<Rule>.processPairs(): List<Rule> = mapIndexedNotNull map@ { i, rule1 ->
        when (rule1) {
            is Disjunction -> {
                rule1.body
                    .filter { subRule1 -> allExcept(i) { rule2 -> !(subRule1 contradicts rule2) } }
                    .let { Disjunction(it).simplify() }
                    .apply { forEachExcept(i) { rule2 -> if (rule2 implies rule1) return@map null } }
            }
            else -> {
                forEachIndexed fr@ { j, rule2 ->
                    if (i == j) return@fr
                    if (rule2 implies rule1) return@map null
                    if (j > i && (rule1 contradicts rule2)) return listOf(Contradiction)
                }
                rule1
            }
        }
    }

    override fun unaryMinus() = Disjunction(body.map { -it }.toSet())

    override fun _intersect(other: Rule) = if (other is Conjunction) Conjunction(this.body.union(other.body))
                                           else                      Conjunction(this.body + other)

    override fun _contradicts(other: Rule): Boolean? = when (other) {
        is Conjunction -> contradictsConjunction(other)
        is Disjunction -> contradictsDisjunction(other)
        else           -> body.any { it contradicts other }
    }

    private fun contradictsConjunction(other: Conjunction): Boolean {
        return this.body.any { rule1 -> other.body.any { rule2 -> rule1 contradicts rule2 } }
    }

    private fun contradictsDisjunction(other: Disjunction): Boolean {
        var contradictions = 0
        other.body.forEach { rule1 ->
            if (this.body.any { rule2 -> rule1 contradicts rule2 }) contradictions++
        }
        return contradictions == other.body.size
    }
}