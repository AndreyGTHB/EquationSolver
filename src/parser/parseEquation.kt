package parser

import equations.Equation

fun String.parseEquation(aim: Char, considerDomain: Boolean = true): Equation {
    val (left, right) = split('=')
    return Equation(left.parseExpression() to right.parseExpression(), considerDomain, aim)
}

fun String.parseEquation(considerDomain: Boolean = true): Equation {
    val (left, right) = split('=')
    return Equation(left.parseExpression() to right.parseExpression(), considerDomain)
}
