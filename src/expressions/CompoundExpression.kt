package expressions

import statements.Statement
import statements.Tautology

abstract class CompoundExpression (
    domain: Statement = Tautology,
    final: Boolean = false
) : Expression(domain, final) {
    protected var bodyDomain: Statement = Tautology
    override fun _fullDomain() = bodyDomain * domain

    abstract override fun firstVariable(): Char?
    abstract override fun contains(variable: Char): Boolean
    abstract override fun _substitute(variable: Char, value: Expression): Expression
}