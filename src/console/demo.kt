package console

fun main() {
    demoColors256("${Clr.fg(121)}fg(color)${Clr.RC}", Clr::fg)
    demoColors256("${Clr.fg(227)}bg(color)${Clr.RC}", Clr::bg)
}

private fun demoColors256(functionName: String, colorFunction: (Int) -> String ) { // When called shows all 256 colors and they respectively numbers. --> gnc.gnc.console.console.colorTest()
    val red = (0..255).random()
    val blue = (0..255).random()
    repeat(256) { green ->
        val color = Clr.rgbFg(red, green, blue)
        val background = Clr.rgbBg(red xor 255, green xor 255, blue xor 255)
        print("$color$background*")
    }

    println("\n** ${Clr.U+ Clr.B}$functionName\n${Clr.RC}")
    var color = 0
    for (l in 0..15) {
        for (c in 0..15) {
            print("${colorFunction(color)}■${color.toString().padEnd(3, ' ')}   ${Clr.RC}")
            color += 1
        }
        println()
    }

    println()

    color = 16
    val step = 36 / 6
    var tabs = ""

    repeat(6) {
        repeat(6) {
            print(tabs)
            repeat(step) {
                print("${colorFunction(color)}■${color.toString().padEnd(3, ' ')} ${Clr.RC}")
                color += 1
            }; println()
        }
        tabs += "\t\t\t\t\t\t\t"
    }
    println()
}
