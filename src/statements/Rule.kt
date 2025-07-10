package statements

import console.Colourable

abstract class Rule : Comparable<Rule>, Colourable {
    abstract val body: Any?

    abstract fun simplify(): Rule

    protected open fun _intersect(other: Rule): Rule? = if (other is Conjunction) Conjunction(other.body + this)
                                                        else                      Conjunction(this, other)
    operator fun times(other: Rule): Rule {
        if (this == Contradiction || other == Contradiction) return Contradiction
        return this._intersect(other) ?: other._intersect(this) ?: Contradiction
    }

    protected open fun _union(other: Rule): Rule? = if (other is Disjunction) Disjunction(other.body + this)
                                                    else                      Disjunction(this, other)
    operator fun plus(other: Rule): Rule {
        if (this == Tautology || other == Tautology) return Tautology
        return this._union(other) ?: other._union(this) ?: Contradiction
    }

    override fun hashCode() = body.hashCode()
    override fun equals(other: Any?): Boolean {
        if (other == null) return false
        if (other::class != this::class) return false
        other as Rule
        return other.body == this.body
    }

    override fun compareTo(other: Rule) = this.toString() compareTo other.toString()

    abstract operator fun unaryMinus(): Rule

    abstract override fun toString(): String
}