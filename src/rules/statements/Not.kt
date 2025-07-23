package rules.statements

import console.Clr
import console.coloured
import rules.Contradiction
import rules.Tautology

class Not (val statement: Statement) : Statement(statement.body) {
    override fun _simplify() = when (val it = statement.simplify()) {
        is Contradiction -> Tautology
        is Tautology     -> Contradiction
        is Not           -> it.statement
        else             -> Not(it as Statement)
    }

    override fun unaryMinus() = statement

    override fun _1contradicts(other: Statement) = if (statement == other) true
                                                   else                    null

    override fun toString() = "! $statement"

    override fun coloured() = "! ".coloured(Clr.NOT) + statement.coloured()


}