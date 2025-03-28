package expressions

interface Reducible {
    fun reduceBy(other: Expression): Expression
    fun commonFactor(other: Expression): Expression
}