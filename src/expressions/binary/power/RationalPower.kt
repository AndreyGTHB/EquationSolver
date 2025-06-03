package expressions.binary.power

import expressions.Expression
import expressions.number.Rational

internal class RationalPower (
    override val body: Pair<Expression, Rational>, final: Boolean = false
) : Power(body, final) {
    override val exponent = body.second
}