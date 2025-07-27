package parser

import equations.Equation

fun String.parseEquation(considerDomain: Boolean = true): Equation {
    val (left, right) = split('=')
    return Equation(left.parseExpression() to right.parseExpression(), considerDomain)
}
