package console

const val esc = "\u001b["

object Clr {

    const val RC = "${esc}0m" // Reset foreground and background colors. --> $gnc.gnc.console.console.RC

    const val R = "${esc}7m"  // Invert foreground to background
    const val U = "${esc}4m"  // Underline
    const val B = "${esc}1m"  // Bold
    const val I = "${esc}3m"  // Italic
    const val S = "${esc}9m"  // Strikethrough the text

    const val WHITE = "${esc}38;5;7m"
    const val RED = "${esc}38;5;1m"
    const val GREEN = "${esc}38;5;46m"
    const val YELLOW = "${esc}38;5;227m"
    const val BLUE = "${esc}38;5;27m"
    const val ORANGE = "${esc}38;5;208m"
    const val ERR = RED
    const val WARN = YELLOW
    const val SUCCESS = GREEN

    // 256 Colors
    fun fg(n: Int) = "${esc}38;5;${n}m" // Set a foreground color.
    fun bg(n: Int) = "${esc}48;5;${n}m" // Set a background color.

    // RGB Colors
    fun rgbFg(r: Int, g: Int, b: Int) = "${esc}38;2;$r;$g;${b}m" // Set a RGB foreground color.
    fun rgbBg(r: Int, g: Int, b: Int) = "${esc}48;2;$r;$g;${b}m" // Set a RGB background color.

    // predefined "nice" colors
    val palette = arrayOf(120, 229, 135, 4, 5, 6, 249, 179, 227)

    fun String.highlight(baseColor: String? = null): String {
        val clr = baseColor ?: RC
        return clr + this
            .replace("✔", "${SUCCESS}✔${clr}")
            .replace("❌", "${ERR}❌${clr}")
            .replace("⚠", "${WARN}⚠${clr}")
            .replace("⮕", "${BLUE}⮕${clr}")
            .replace("⊗", "${ORANGE}⊗${clr}")
    }

}