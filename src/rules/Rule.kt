package rules

import console.Colourable
import rules.msets.EmptySet

abstract class Rule : Comparable<Rule>, Colourable {
    abstract val body: Any?
    var final = false
        private set

    protected abstract fun _simplify(): Rule
    fun simplify(): Rule {
        if (final) return this
        return _simplify().apply { final = true }
    }

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

    open operator fun unaryMinus(): Rule = Complement(this)

    protected abstract fun _contradicts(other: Rule): Boolean?
    open infix fun contradicts(other: Rule): Boolean {
        assert(this.final && other.final)
        assert(this !is Contradiction && other !is Contradiction)
        return if (this is Contradiction || other is Contradiction
                || this is Tautology || other is Tautology) false
          else this._contradicts(other) ?: other._contradicts(this) ?: false
    }

    protected open fun _implies(other: Rule): Boolean = this contradicts (-other).simplify()
    infix fun implies(other: Rule): Boolean {
        assert(this.final && other.final)
        return _implies(other)
    }

    override fun hashCode() = body.hashCode()
    override fun equals(other: Any?): Boolean {
        if (other == null) return false
        if (other::class != this::class) return false
        other as Rule
        return other.body == this.body
    }

    override fun compareTo(other: Rule) = this.toString() compareTo other.toString()

    abstract override fun toString(): String
}