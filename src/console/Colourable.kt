package console

interface Colourable {
    fun coloured(): String
    fun printlnColoured() { println(coloured()) }
}


fun Collection<Colourable>.colouredUnder(name: String, nameColour: Int): String {
    var coloured = "$name:".coloured(nameColour)
    forEach { element ->
        val subExprString = element.coloured()
            .split("\n")
            .joinToString("\n") { "  $it" }
            .run { "\u00b7".coloured(nameColour) + slice(1 .. lastIndex) }
        coloured += "\n" + subExprString
    }
    return coloured
}

fun Collection<*>.toStringUnder(name: String): String {
    var str = "$name:("
    forEach { str += "$it " }
    str = str.slice(0 until str.lastIndex) + ')'
    return str
}

fun String.coloured(colour: Int) = Clr.fg(colour) + this + Clr.RC
