package expressions

import expressions.binary.Power
import expressions.binary.Quotient
import expressions.longs.Product
import expressions.longs.Sum
import expressions.number.Rational

@Suppress("FunctionName")
abstract class Expression (open val final: Boolean = false) : Comparable<Expression> {
    abstract val body: Any
    private var isNumber: Boolean? = null

    abstract fun simplify(): Expression

    protected open fun _isNumber(): Boolean = false
    fun isNumber(): Boolean {
        if (isNumber == null) isNumber = _isNumber()
        true xor false
        return isNumber!!
    }

    internal open fun commonFactor(other: Expression): Expression? = null

    protected open fun _reduceOrNull(other: Expression): Expression? = null
    fun reduceOrNull(other: Expression): Expression? {
        if (!(this.final && other.final)) TODO("Reducing non-simplified expressions")
        if (other.isZeroRational()) TODO("Reducing by zero")
        if (other.isUnitRational() || this.isZeroRational()) return this
        return _reduceOrNull(other)?.simplify()
    }
    fun reduce(other: Expression): Expression = reduceOrNull(other)!!

    open fun rationalPart(): Rational = unit()
    open fun nonRationalPart(): Expression = this
    open fun numericalPart(): Expression = if (isNumber()) this else unit()
    open fun nonNumericalPart(): Expression = if (isNumber()) unit() else this

    override fun equals(other: Any?): Boolean {
        if (other == null) return false
        if (other::class != this::class) return false
        other as Expression
        return this.body == other.body
    }
    override fun hashCode(): Int = body.hashCode()

    override fun compareTo(other: Expression): Int {
        val typeComparisonCode = compareExpressionTypes(this, other)
        return if (typeComparisonCode == 0) this.toString() compareTo other.toString()
          else                              typeComparisonCode
    }

    open operator fun unaryMinus(): Expression = (-unit()) * this
    open operator fun plus(other: Expression)  = Sum(listOf(this, other))
    open operator fun minus(other: Expression) = Sum(listOf(this, -other))
    open operator fun times(other: Expression) = Product(listOf(this, other))
    open operator fun div(other: Expression)   = Quotient(this to other)
    infix fun raisedTo(other: Expression)      = Power(this to other)

    abstract override fun toString(): String
}


fun Expression.isUnitRational(): Boolean = this is Rational && this.isUnit()
fun Expression.isZeroRational(): Boolean = this is Rational && this.isZero()
