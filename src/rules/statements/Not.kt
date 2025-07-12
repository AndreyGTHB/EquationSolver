package rules.statements

import console.Clr
import console.coloured

class Not (val statement: Statement) : Statement(statement.body) {
    override fun simplify() = when (val it = statement.simplify()) {
        is Not -> it.statement
        else   -> it
    }

    override fun unaryMinus() = statement

    override fun _contradicts(other: Statement) = null

    override fun toString() = "! $statement"

    override fun coloured() = "! ".coloured(Clr.NOT) + statement.coloured()


}