package statements

class Disjunction(body: Set<Statement>) : LongStatement(body) {
    constructor(vararg body: Statement) : this(body.toSet())

    override fun simplify(): Statement {
        TODO("Not yet implemented")
    }

    override fun _intersect(other: Statement): Statement {
        return when (other) {
            is Conjunction -> Conjunction(other.body + this)
            else           -> Conjunction(this, other)
        }
    }

    override fun unaryMinus() = Conjunction(body.map { -it }.toSet())

}