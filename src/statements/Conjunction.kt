package statements

class Conjunction (val statements: Set<Statement>) : StatementSet() {
    constructor(vararg statements: Statement) : this(statements.toSet())

    override fun simplify() = if (statements.isEmpty()) UniversalSet
                         else if (statements.size == 1) statements.first().simplify()
                         else                           this

    override fun _intersect(other: StatementSet): Conjunction? { return when (other) {
        is Statement   -> Conjunction(statements + other)
        is Conjunction -> Conjunction(this.statements.union(other.statements))
        else           -> null
    }}

    override fun unaryMinus() = TODO()
}