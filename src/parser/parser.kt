package parser

import expressions.Expression
import expressions.binary.Power
import expressions.binary.Quotient
import expressions.longs.Product
import expressions.longs.Sum
import expressions.monomials.Monomial
import expressions.number.Rational
import expressions.number.Real
import expressions.number.toRational
import expressions.unit
import expressions.zero
import utils.isInt

const val expressionSigns = "+-*/^"

fun String.parse() = standardize()._parse()

@Suppress("FunctionName")
fun String._parse(): Expression {
    if (last() == ')' && indexOfOpeningBracket(lastIndex) == 0) return slice(1 ..< lastIndex)._parse()

    val parsed = parseSum() ?: parseProductOrQuotient() ?: parsePower()
    return parsed ?: if (isInt()) toInt().toRational() else toMonomial()
}

private fun String.parseSum(): Expression? {
    val body = mutableListOf<Expression>()
    var exprEnd = lastIndex
    forEachIndexedOutsideBracketsReverse { i, c ->
        if (c == '+') { body.add(0, slice(i+1 .. exprEnd)._parse()); exprEnd = i - 1 }
        if (c == '-') { body.add(0, -slice(i+1 .. exprEnd)._parse()); exprEnd = i - 1  }
    }
    if (exprEnd == lastIndex) return null
    if (exprEnd != -1) body.add(0, slice(0 .. exprEnd)._parse())
    return if (body.size > 1) Sum(body) else body[0]
}

private fun String.parseProductOrQuotient(): Expression? {
    val numerBody = mutableListOf<Expression>()
    val denomBody = mutableListOf<Expression>()
    var exprEnd = lastIndex
    forEachIndexedOutsideBracketsReverse { i, c ->
        if (c == '*') { numerBody.add(0, slice(i+1 .. exprEnd)._parse()); exprEnd = i - 1 }
        if (c == '/') { denomBody.add(0, slice(i+1 .. exprEnd)._parse()); exprEnd = i - 1 }
    }
    if (exprEnd == lastIndex) return null
    numerBody.add(0, slice(0 .. exprEnd)._parse())

    val numer = if (numerBody.size > 1) Product(numerBody) else numerBody[0]
    val denom = if (denomBody.size > 1)  Product(denomBody)
           else if (denomBody.size == 1) denomBody[0]
           else                          null

    return if (denom == null) numer
      else if (numer is Rational && denom is Rational
            && numer.isInteger() && denom.isInteger()) numer / denom
      else Quotient(numer to denom)
}

private fun String.parsePower(): Expression? {
    forEachIndexedOutsideBracketsReverse { i, c -> if (c == '^') {
        val base = slice(0 ..< i)._parse()
        val exp = slice(i+1 ..< length)._parse()
        return if (base is Rational && base.denom == 1 && exp is Rational) Real(base.numer to exp) else Power(base to exp)
    }}
    return null
}

fun String.standardize() = this
    .removeSpaces()
    .removeExtraStars()
    .insertMissingStars()

private fun String.removeSpaces() = replace(" ", "")

private fun String.removeExtraStars(): String {
    if (length < 3) return this

    var newString = ""
    subStrings(3).forEach {
        val (l, c, r) = it.toCharArray()
        if (l == '*') return@forEach

        newString += l
        if (c == '*' && !(l.isLetter() && r.isLetter())) newString += '*'
    }
    if (this[lastIndex-1] != '*') newString += this[lastIndex-1]
    newString += last()
    return newString
}

private fun String.insertMissingStars(): String {
    var newString = ""
    subStrings(2).forEach {
        val (l, r) = it.toCharArray()
        newString += l
        if (areIntersect(it, expressionSigns)) return@forEach

        if ((!l.isDigit() || !r.isDigit())
            && (!l.isLetter() || !r.isLetter())
            && l != '(' && r != ')') newString += '*'
    }
    newString += last()
    return newString
}

private inline fun String.forEachIndexedOutsideBracketsReverse(action: (Int, Char) -> Unit) {
    var i = lastIndex
    while (i >= 0) {
        val c = this[i]
        if (c == ')') {
            i = indexOfOpeningBracket(i) - 1
            continue
        }
        action(i, c)
        i--
    }
}

private fun String.indexOfOpeningBracket(indexOfClosing: Int): Int {
    var stack = 0
    for (i in indexOfClosing downTo 0) {
        val c = this[i]
        if (c == ')') stack++
        if (c == '(') stack--
        if (stack == 0) return i
    }
    return -1
}

private fun String.toMonomial(): Monomial {
    val varMap = mutableMapOf<Char, Rational>()
    forEach {
        varMap[it] = (varMap[it] ?: zero()) + unit()
    }
    return Monomial(varMap)
}

fun areIntersect(str1: String, str2: String) = str1.any { it in str2 }
