package equations

import rules.statements.Statement
import rules.Rule
import rules.Tautology
import rules.msets.EmptySet
import rules.msets.Universe
import rules.statements.belongsTo

data class Solution (val answer: Statement, val domain: Rule = Tautology)

fun universalSolution(variable: Char) = Solution(variable belongsTo Universe)
fun emptySolution(variable: Char) = Solution(variable belongsTo EmptySet)
