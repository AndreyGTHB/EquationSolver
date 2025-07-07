package expressions

import statements.StatementSet
import statements.UniversalSet

abstract class CompoundExpression (
    domain: StatementSet = UniversalSet,
    final: Boolean = false
) : Expression(domain, final) {
    protected var bodyDomain: StatementSet = UniversalSet
    override fun _fullDomain() = bodyDomain * domain

    abstract override fun firstVariable(): Char?
    abstract override fun contains(variable: Char): Boolean
    abstract override fun _substitute(variable: Char, value: Expression): Expression
}