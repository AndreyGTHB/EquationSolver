package statements

class Conjunction (body: Set<Statement>) : LongStatement(body) {
    constructor(vararg body: Statement) : this(body.toSet())

    override fun simplify(): Statement {
        val newBody = simplifyBody()
            .expandConjunctions()
            .toSortedSet()
        return when (newBody.size) {
            0    -> throw RuntimeException("Empty long statement")
            1    -> newBody.first()
            else -> Conjunction(newBody)
        }
    }

    private fun Set<Statement>.expandConjunctions(): Set<Statement> {
        val newBody = mutableSetOf<Statement>()
        forEach {
            if (it is Conjunction) newBody.addAll(it.body)
            else                   newBody.add(it)
        }
        return newBody
    }

    override fun _intersect(other: Statement): Conjunction {
        return when (other) {
            is Conjunction -> Conjunction(this.body.union(other.body))
            else           -> Conjunction(body + other)
        }
    }

    override fun unaryMinus() = Disjunction(body.map { -it }.toSet())
}