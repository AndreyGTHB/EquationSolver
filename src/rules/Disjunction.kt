package rules

import utils.allExcept
import utils.allIndexed

class Disjunction(body: Collection<Rule>) : LongRule(body.toSet()) {
    constructor(vararg body: Rule) : this(body.toSet())

    override fun _simplify(): Rule {
        val newBody = simplifyBody()
            .expandDisjunctions()
            .checkForTautologiesAndContradictions()
            .processPairs()
            .toSortedSet()
        return when (newBody.size) {
            0    -> Contradiction
            1    -> newBody.first()
            else -> Disjunction(newBody)
        }
    }

    private fun Set<Rule>.expandDisjunctions() = flatMap { if (it is Disjunction) it.body else listOf(it) }.toSet()

    private fun Set<Rule>.checkForTautologiesAndContradictions(): List<Rule> = filter {
        if (it is Tautology) return listOf(Tautology)
        it !is Contradiction
    }

    private fun List<Rule>.processPairs(): List<Rule> = mapIndexedNotNull { i, rule1 ->
        rule1.let { rule1 ->
            if (rule1 is Conjunction) {
                rule1.body
                    .filter { subRule1 -> allExcept(i) { rule2 -> subRule1 != (-rule2).simplify() } }
                    .let { Conjunction(it).simplify() }
            }
            else rule1
        }.let { rule1 ->
            rule1.takeIf {
                allIndexed { j, rule2 ->
                    if (i < j && rule1 == (-rule2).simplify()) return listOf(Tautology)
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