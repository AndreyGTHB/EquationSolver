package expressions

import rules.Rule
import rules.Tautology

abstract class CompoundExpression (
    domain: Rule = Tautology,
    final: Boolean = false
) : Expression(domain, final) {
    protected var bodyDomain: Rule = Tautology
    override fun _fullDomain() = bodyDomain * domain

    abstract override fun firstVariable(): Char?
    abstract override fun contains(variable: Char): Boolean
    abstract override fun _substitute(variable: Char, value: Expression): Expression
}