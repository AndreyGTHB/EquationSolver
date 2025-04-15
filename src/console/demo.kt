package console

import console.Clr.highlight

fun main() {
    println(
        "Let's see what can we do with colors!".map { char ->
            val color = Clr.fg(Clr.palette.random())
            color + char
        }.joinToString("")
    )
    println(Clr.RC)
    println("⚠ Highlight special symbols using ${Clr.B+Clr.U+Clr.ORANGE}String${Clr.RC+Clr.B+Clr.YELLOW}.highlight()${Clr.RC} function. Works for: '❌', '⚠', '⮕', '⊗'".highlight())
    println(Clr.RC)

    demoColors256("${Clr.fg(121)}Foreground colors - ${Clr.U+Clr.B}fun fg(color)${Clr.RC}") {
        color256Index -> Clr.fg(color256Index)
    }

    demoColors256("${Clr.fg(227)}Background colors - ${Clr.U+Clr.B}fun bg(color)${Clr.RC}") {
        color256Index -> Clr.bg(color256Index)
    }
}

private fun demoColors256(functionName: String, colorFunction: (Int) -> String,  ) { // When called shows all 256 colors and they respectively numbers. --> gnc.gnc.console.console.colorTest()
    val red = (0..255).random()
    val blue = (0..255).random()
    repeat(256) { green ->
        val color = Clr.rgbFg(red, green, blue)
        val background = Clr.rgbBg(red xor 255, green xor 255, blue xor 255)
        print("$color$background*")
    }

    println("${Clr.RC}\n** $functionName\n${Clr.RC}")
    var color256Index = 0
    repeat(16) {
        val color = colorFunction(color256Index)
        val colorIndexFixedLength = color256Index.toString().padEnd(3, ' ')
        print("${color} ■${colorIndexFixedLength} ${Clr.RC} ")
        color256Index += 1
    }
    println()

    repeat(36) { l ->
        repeat(6) { c ->
            val color = colorFunction(color256Index)
            val colorIndexFixedLength = color256Index.toString().padEnd(3, ' ')
            print("${color} ■${colorIndexFixedLength} ${Clr.RC} ")
            color256Index += 1
        }
        println()
    }

    println()

}
