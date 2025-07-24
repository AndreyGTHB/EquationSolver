package rules

import utils.allIndexed

class Disjunction(body: Set<Rule>) : LongRule(body) {
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

    private fun List<Rule>.processPairs(): List<Rule> = filterIndexed { i, rule1 ->
        when (rule1) {
            is Conjunction -> {
                rule1.body.all { subRule1 ->
                    this.allIndexed { j, rule2 -> i == j || !(subRule1 implies rule2) } // NOPT: (some pairs may include already removed rules)
                }
            }
            else -> {
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
        return this.body.all { subRule1 -> other.body.all { subRule2 -> subRule1 contradicts subRule2 } }
    }
}