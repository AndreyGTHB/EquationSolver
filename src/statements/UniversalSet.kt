package statements

object UniversalSet : StatementSet() {
    override fun simplify() = this
    override fun _intersect(other: StatementSet) = other
    override fun unaryMinus() = EmptySet
}