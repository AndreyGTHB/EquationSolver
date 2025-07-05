package statements

object EmptySet : StatementSet() {
    override fun simplify() = this
    override fun _intersect(other: StatementSet) = this
    override fun unaryMinus() = UniversalSet
}