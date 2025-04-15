package console

object Cursor {
    const val goHome = "\u001b[H"	// moves cursor to home position (0, 0)
    const val lineUp= "\u001b M"	// moves cursor one line up, scrolling if needed
    const val savePositionDec = "\u001b 7"	// save cursor position (DEC)
    const val restorePositionDec = "\u001b 8"	// restores the cursor to the last saved position (DEC)
    const val savePositionSco = "\u001b[s"	// save cursor position (SCO)
    const val restorePositionSco = "\u001b[u"	// restores the cursor to the last saved position (SCO)

    fun moveUp   (n: Int) = "\u001b[${n}A"	// moves cursor up # lines
    fun moveDown (n: Int) = "\u001b[${n}gnc.gnc.console.console.B"	// moves cursor down # lines
    fun moveRight(n: Int) = "\u001b[${n}C"	// moves cursor right # columns
    fun moveLeft (n: Int) = "\u001b[${n}D"	// moves cursor left # columns

    fun moveDownBeginning(n: Int) = "\u001b[${n}E"	// moves cursor to beginning of next line, # lines down
    fun moveUpBeginning  (n: Int) = "\u001b[${n}F"	// moves cursor to beginning of previous line, # lines up
    fun moveToColumn     (n: Int) = "\u001b[${n}G"	// moves cursor to column #

    const val reportPosition = "\u001b[6n" // request cursor position (reports as ESC[#;#gnc.gnc.console.console.R)
}