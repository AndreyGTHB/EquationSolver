package parser

import equations.Equation

fun String.parseEquation(): Equation {
    val (left, right) = split('=')
    return Equation(left.parseExpression(), right.parseExpression())
}
