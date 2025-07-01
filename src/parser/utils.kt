package parser

inline fun String.forEachIndexedReversed(action: (Int, Char) -> Unit) {
    for (i in lastIndex downTo 0) action(i, this[i])
}
