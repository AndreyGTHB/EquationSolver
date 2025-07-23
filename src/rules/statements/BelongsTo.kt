package rules.statements

import console.Clr
import console.coloured
import rules.Contradiction
import rules.msets.MathSet
import rules.Rule
import rules.Tautology
import rules.msets.EmptySet
import rules.msets.Universe

class BelongsTo (variable: Char, val set: MathSet) : Statement(variable, set) {
    override fun _1contradicts(other: Statement) = when (other) {
        is Not if other.statement is BelongsTo -> this.set in other.statement.set
        is BelongsTo -> this.set * other.set == EmptySet
        is EqualsTo  -> other.expr.isNumber && other.expr !in set
        else         -> null
    }

    override fun _simplify(): Rule = when (set) {
        is EmptySet -> Contradiction
        is Universe -> Tautology
        else        -> this
    }

    override fun unaryMinus(): Rule {
        TODO("Not yet implemented")
    }

    override fun toString() = "$variable \u2208 $set"
    override fun coloured() = toString().coloured(Clr.BELONGING)

}

infix fun Char.belongsTo(set: MathSet) = BelongsTo(this, set)
infix fun Char.notBelongsTo(set: MathSet) = Not(BelongsTo(this, set))
