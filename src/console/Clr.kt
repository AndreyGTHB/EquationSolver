package console

object Clr {

    const val RC = "\u001b[0m" // Reset foreground and background colors. --> $gnc.gnc.console.console.RC

    const val R = "\u001b[7m"  // Invert foreground to background. --> $gnc.gnc.console.console.R
    const val U = "\u001b[4m"  // Underline. --> $gnc.gnc.console.console.U
    const val B = "\u001b[1m"  // Bold. --> $gnc.gnc.console.console.B
    const val I = "\u001b[3m"  // Italic. --> $gnc.gnc.console.console.I
    const val S = "\u001b[9m"  // Strikethrough the text. --> $gnc.gnc.console.console.S

    const val WHITE = "\u001b[38;5;7m"
    const val RED = "\u001b[38;5;1m"
    const val ERR = RED
    const val YELLOW = "\u001b[38;5;227m"
    const val WARN = YELLOW
    const val BLUE = "\u001b[38;5;27m"
    const val ORANGE = "\u001b[38;5;208m"

    // 256 Colors
    fun fg(n: Int) = "\u001b[38;5;${n}m" // Set a foreground color. --> ${gnc.gnc.console.console.fg(40)} //Sets a green color.
    fun bg(n: Int) = "\u001b[48;5;${n}m" // Set a background color. --> ${gnc.gnc.console.console.bg(196)} //Sets a red color.

    // RGB Colors
    fun rgbFg(r: Int, g: Int, b: Int) = "\u001b[38;2;$r;$g;${b}m" // Set a RGB foreground color. --> ${gnc.gnc.console.console.rgbfg(0,255,0)} //Sets a green color.
    fun rgbBg(r: Int, g: Int, b: Int) = "\u001b[48;2;$r;$g;${b}m" // Set a RGB background color. --> ${gnc.gnc.console.console.rgbbg(255,0,0)} //Sets a red color.

    // predefined "nice" colors
    private val palette = arrayOf(120, 229, 135, 4, 5, 6, 249, 179, 227)
    fun std(index: Int): Int = palette.getOrElse(index) { 103 + index }

    fun String.highlight(baseColor: String): String {
        return baseColor + this
            .replace("❌", "${ERR}❌${baseColor}")
            .replace("⚠", "${WARN}⚠${baseColor}")
            .replace("⮕", "${BLUE}⮕${baseColor}")
            .replace("⊗", "${ORANGE}⊗${baseColor}")
    }

}