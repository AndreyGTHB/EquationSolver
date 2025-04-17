package console

object Cursor {
    const val goHome = "${esc}H"	// moves cursor to home position (0, 0)
    const val lineUp= "\u001b M"	// moves cursor one line up, scrolling if needed
    const val savePositionDec = "\u001b 7"	// save cursor position (DEC)
    const val restorePositionDec = "\u001b 8"	// restores the cursor to the last saved position (DEC)
    const val savePositionSco = "${esc}s"	// save cursor position (SCO)
    const val restorePositionSco = "${esc}u"	// restores the cursor to the last saved position (SCO)

    fun moveUp   (n: Int) = "${esc}${n}A"	// moves cursor up # lines
    fun moveDown (n: Int) = "${esc}${n}B"	// moves cursor down # lines
    fun moveRight(n: Int) = "${esc}${n}C"	// moves cursor right # columns
    fun moveLeft (n: Int) = "${esc}${n}D"	// moves cursor left # columns

    fun moveDownBeginning(n: Int) = "${esc}${n}E"	// moves cursor to beginning of next line, # lines down
    fun moveUpBeginning  (n: Int) = "${esc}${n}F"	// moves cursor to beginning of previous line, # lines up
    fun moveToColumn     (n: Int) = "${esc}${n}G"	// moves cursor to column #

    const val reportPosition = "${esc}6n" // request cursor position (reports as ESC[#;#R)
}