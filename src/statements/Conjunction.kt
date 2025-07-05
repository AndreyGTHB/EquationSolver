package statements

class Conjunction (val statements: Set<Statement>) : StatementSet() {
    constructor(vararg statements: Statement) : this(statements.toSet())

    override fun simplify() = if (statements.isEmpty()) UniversalSet
                         else if (statements.size == 1) statements.first().simplify()
                         else                           this

    override fun _intersect(other: StatementSet): Conjunction? {
        val otherAsConjunction = (other as? Conjunction) ?: return null
        return Conjunction(this.statements.union(other.statements))
    }

    override fun unaryMinus() = TODO()
}