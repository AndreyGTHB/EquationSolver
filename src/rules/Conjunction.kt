package rules

class Conjunction (body: Set<Rule>) : LongRule(body) {
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

    private fun List<Rule>.processPairs(): List<Rule> = mapIndexed { i, rule1 ->
        when (rule1) {
            is Disjunction -> {
                rule1.body
                    .filter { subRule1 -> drop(i+1).all { rule2 -> !(subRule1 contradicts rule2) } }
                    .toSet()
                    .let { Disjunction(it).simplify() }
            }
            else -> {
                if (drop(i+1).any { rule2 -> rule1 contradicts rule2 }) return listOf(Contradiction)
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
        return this.body.any { subRule1 -> other.body.any { subRule2 -> subRule1 contradicts subRule2 } }
    }

    private fun contradictsDisjunction(other: Disjunction): Boolean {
        return this.body.any { subRule1 -> other.body.all { subRule2 -> subRule1 contradicts subRule2 } }
    }
}