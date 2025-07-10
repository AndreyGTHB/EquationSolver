package statements

class Conjunction (body: Set<Rule>) : LongRule(body) {
    constructor(vararg body: Rule) : this(body.toSet())

    override fun simplify(): Rule {
        val newBody = simplifyBody()
            .expandConjunctions()
            .checkForContradictions()
            .toSortedSet()
        return when (newBody.size) {
            0    -> throw RuntimeException("Empty long rule")
            1    -> newBody.first()
            else -> Conjunction(newBody)
        }
    }

    private fun Set<Rule>.expandConjunctions(): Set<Rule> {
        val newBody = mutableSetOf<Rule>()
        forEach {
            if (it is Conjunction) newBody.addAll(it.body)
            else                   newBody.add(it)
        }
        return newBody
    }

    private fun Set<Rule>.checkForContradictions(): Set<Rule> {
        val statementMap = mutableMapOf<Char, MutableSet<Statement>>()
        forEach {
            when (it) {
                is Contradiction -> return setOf(Contradiction)
                is Statement     -> {
                    if (statementMap[it.variable] == null) statementMap[it.variable] = mutableSetOf()
                    statementMap[it.variable]!!.add(it)
                }
            }
        }
        // ToDo: Checking for contradicting statements
        return this
    }

    override fun _intersect(other: Rule) = if (other is Conjunction) Conjunction(this.body.union(other.body))
                                           else                      Conjunction(this.body + other)

    override fun unaryMinus() = Disjunction(body.map { -it }.toSet())
}