package rules

import utils.allExcept
import utils.allIndexed

class Disjunction(body: Collection<Rule>) : LongRule(body.toSet()) {
    constructor(vararg body: Rule) : this(body.toSet())

    override fun _simplify(): Rule {
        val newBody = simplifyBody()
            .toList()
            .clean()
            .processPairs()
            .clean()
            .toSortedSet()
        return when (newBody.size) {
            0    -> Contradiction
            1    -> newBody.first()
            else -> Disjunction(newBody)
        }
    }

    private fun List<Rule>.clean() = this
        .flatMap { if (it is Disjunction) it.body else listOf(it) }
        .distinct()
        .filter {
            if (it is Tautology) return listOf(Tautology)
            it !is Contradiction
        }

    private fun List<Rule>.processPairs(): List<Rule> = mapIndexedNotNull { i, rule1 ->
        rule1.let { rule1 ->
            if (rule1 is Conjunction) {
                rule1.body
                    .filter { subRule1 -> allExcept(i) { rule2 -> !((-subRule1).simplify() implies rule2) } }
                    .let { if (it.isNotEmpty()) Conjunction(it).simplify() else Tautology }
            }
            else rule1
        }.let { rule1 ->
            rule1.takeIf {
                allIndexed { j, rule2 ->
                    if (i < j && (-rule1).simplify() implies rule2) return listOf(Tautology)
                    i == j || !(rule1 implies rule2)
                }
            }
        }
    }

    override fun unaryMinus() = Conjunction(body.map { -it }.toSet())

    override fun _union(other: Rule): Rule {
        return if (other is Disjunction) Disjunction(this.body + other.body)
               else                      Disjunction(this.body + other)

    }

    override fun _contradicts(other: Rule): Boolean? = when (other) {
        is Disjunction -> contradictsDisjunction(other)
        else           -> body.all { it contradicts other }
    }

    private fun contradictsDisjunction(other: Disjunction): Boolean {
        return this.body.all { rule1 -> other.body.all { rule2 -> rule1 contradicts rule2 } }
    }
}