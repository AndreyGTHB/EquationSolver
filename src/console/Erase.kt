package console

object Erase {
    const val inDisplay                          = "${esc}J"   // erase in display (same as ESC[0J)
    const val FROM_CURSOR_UNTIL_END_OF_SCREEN    = "${esc}0J"  // erase from cursor until end of screen
    const val FROM_CURSOR_TO_BEGINNING_OF_SCREEN = "${esc}1J"  // erase from cursor to beginning of screen
    const val ENTIRE_SCREEN                      = "${esc}2J"  // erase entire screen
    const val SAVED_LINES                        = "${esc}3J"  // erase saved lines
    const val IN_LINE                            = "${esc}K"   // erase in line (same as ESC[0K)
    const val FROM_CURSOR_TO_END_OF_LINE         = "${esc}0K"  // erase from cursor to end of line
    const val START_OF_LINE_TO_THE_CURSOR        = "${esc}1K"  // erase start of line to the cursor
    const val THE_ENTIRE_LINE                    = "${esc}2K"  // erase the entire line
}