package expressions

typealias ExpressionBuilderBlock = ExpressionBuilder.() -> Unit

class ExpressionBuilder(private val start: Expression) {
    var expr = start
        private set

    fun base(other: Expression) { expr = other }

    fun plus(other: Expression) { expr += other }
    fun plus(currStart: Expression = start, block: ExpressionBuilderBlock) {
        expr += buildExpression(currStart, block)
    }

    fun minus(other: Expression) { expr -= other }
    fun minus(currStart: Expression = start, block: ExpressionBuilderBlock) {
        expr -= buildExpression(currStart, block)
    }

    fun times(other: Expression) { expr *= other }
    fun times(currStart: Expression = start, block: ExpressionBuilderBlock) {
        expr *= buildExpression(currStart, block)
    }

    fun div(other: Expression) { expr /= other }
    fun div(currStart: Expression = start, block: ExpressionBuilderBlock) {
        expr /= buildExpression(currStart, block)
    }

    fun raiseTo(other: Expression) { expr = expr raisedTo other }
    fun raiseTo(currStart: Expression = start, block: ExpressionBuilderBlock) {
        expr = expr raisedTo buildExpression(currStart, block)
    }
}

fun buildExpression(start: Expression, block: ExpressionBuilderBlock): Expression {
    val builder = ExpressionBuilder(start).apply(block)
    return builder.expr
}

fun buildExpressionFromZero(block: ExpressionBuilderBlock) = buildExpression(zero(), block)
fun buildExpressionFromUnit(block: ExpressionBuilderBlock) = buildExpression(unit(), block)
