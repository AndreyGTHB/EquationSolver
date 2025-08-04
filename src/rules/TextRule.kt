package rules

import console.Clr
import console.coloured

class TextRule (override val body: String) : Rule() {
    override fun _simplify() = this

    override fun _contradicts(other: Rule) = false

    override fun toString() = "\"$body\""
    override fun coloured() = toString().coloured(Clr.DEFAULT)
}