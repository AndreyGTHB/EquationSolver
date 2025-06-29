package expressions.monomials

import equations.Domain
import equations.FullDomain
import expressions.Expression
import expressions.unit

class Variable(override val body: Char, domain: Domain = FullDomain) : Expression(domain, true) {
    override fun _simplify() = this

    override fun _commonFactor(other: Expression) = if (this == other) this else null

    override fun _reduceOrNull(other: Expression) = if (this == other) unit() else null

    override fun toString() = body.toString()
}