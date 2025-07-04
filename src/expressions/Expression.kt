package expressions

import equations.Domain
import equations.EmptyDomain
import equations.RestrictingDomain
import equations.FullDomain
import equations.Equation
import expressions.binary.Power
import expressions.binary.Quotient
import expressions.longs.Product
import expressions.longs.Sum
import expressions.number.Rational

@Suppress("FunctionName")
abstract class Expression (
    domain: Domain = FullDomain,
    final: Boolean = false
) : Comparable<Expression> {
    companion object {
        fun commonFactor(a: Expression, b: Expression): Expression {
            assert (a.final && b.final)

            if (a is Rational || b is Rational) {
                assert(!(a.isZeroRational() && b.isZeroRational()))
                if (a.isZeroRational()) return b
                if (b.isZeroRational()) return a
                if (a.isUnitRational() || b.isUnitRational()) return unit()
            }

            val cf = a._commonFactor(b) ?: b._commonFactor(a) ?: unit()
            return cf.simplify().apply { domain *= a.domain * b.domain }
        }
    }

    abstract val body: Any?
    var domain = domain
        private set
    var final = final
        protected set

    open val isNumber: Boolean = false

    protected abstract fun _simplify(): Expression
    open fun simplify(): Expression {
        if (final) return this
        val sThis = _simplify()
        val sDomain = _fullDomain()
        return if (sDomain == EmptyDomain) InvalidExpression
               else                        sThis.apply {
                                               domain = sDomain
                                               final = true
                                           }
    }

    protected open fun _fullDomain() = domain
    protected fun addDomainRestriction(restr: Equation) { domain *= RestrictingDomain(restr) }
    protected fun makeInvalid() { domain = EmptyDomain }

    protected open fun _commonFactor(other: Expression): Expression? = null

    protected open fun _reduceOrNull(other: Expression): Expression? = null
    fun reduceOrNull(other: Expression): Expression? {
        assert(this.final && other.final)
        assert(!other.isZeroRational())
        if (other.isUnitRational() || this.isZeroRational()) return this

        val reduced = _reduceOrNull(other)
        return reduced?.simplify()?.also { it.domain *= this.domain * other.domain }
    }
    fun reduce(other: Expression): Expression = reduceOrNull(other)!!

    open fun rationalPart(): Rational = unit()
    open fun nonRationalPart(): Expression = this
    open fun numericalPart(): Expression = if (isNumber) this else unit()
    open fun nonNumericalPart(): Expression = if (isNumber) unit() else this

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


fun Expression.isUnitRational() = this is Rational && this.isUnit()
fun Expression.isZeroRational() = this is Rational && this.isZero()

fun Expression.asProduct() = if (this is Product) this else Product(this)
fun Expression.asSum() = if (this is Sum) this else Sum(this)
