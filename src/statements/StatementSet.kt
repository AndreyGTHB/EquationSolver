package statements

abstract class StatementSet {
    abstract fun simplify(): StatementSet

    protected abstract fun _intersect(other: StatementSet): StatementSet?
    operator fun times(other: StatementSet): StatementSet {
        return this._intersect(other) ?: other._intersect(this) ?: EmptySet
    }

    abstract operator fun unaryMinus(): StatementSet
}