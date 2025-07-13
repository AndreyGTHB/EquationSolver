package expressions

import console.Colourable
import expressions.binary.Power
import expressions.binary.Quotient
import expressions.longs.Product
import expressions.longs.Sum
import expressions.number.Rational
import rules.*
import kotlin.contracts.contract

@Suppress("FunctionName")
abstract class Expression (
    domain: Rule = Tautology,
    final: Boolean = false
) : Comparable<Expression>, Colourable {
    abstract val body: Any?
    var domain = domain
        protected set
    var final = final
        protected set

    open val isNumber = false

    companion object {
        private fun <T : Expression> T.applyLoadingDomainFrom(loader: Expression) = apply { domain = loader.domain }
        private fun <T : Expression> T.applyLoadingDomainFrom(vararg loaders: Expression) = apply {
            domain = loaders.fold(Tautology as Rule) { acc, it -> acc * it.domain }
        }

        fun commonFactor(a: Expression, b: Expression): Expression {
            assert (a.final && b.final)

            if (a is Rational || b is Rational) {
                assert(!(a.isZeroRational() && b.isZeroRational()))
                if (a.isZeroRational()) return b
                if (b.isZeroRational()) return a
                if (a.isUnitRational() || b.isUnitRational()) return unit()
            }

            val cf = a._commonFactor(b) ?: b._commonFactor(a) ?: unit()
            return cf.simplify().applyLoadingDomainFrom(a, b)
        }
    }

    protected abstract fun _simplify(): Expression
    open fun simplify(): Expression {
        if (final) return this
        val sThis = _simplify()
        val sDomain = _fullDomain().simplify()
        return if (sThis == InvalidExpression || sDomain == Contradiction) InvalidExpression
          else sThis.apply {
              domain = sDomain
              final = true
          }
    }

    open fun firstVariable(): Char? = null
    open operator fun contains(variable: Char) = false

    protected open fun _substitute(variable: Char, value: Expression) = this
    fun substitute(variable: Char, value: Expression) = _substitute(variable, value).applyLoadingDomainFrom(this)

    protected open fun _fullDomain() = domain
    protected fun addConstraints(constraints: Rule) { domain *= constraints }
    protected fun makeInvalid() { domain = Contradiction
    }

    protected abstract fun _commonFactor(other: Expression): Expression?

    protected abstract fun _reduceOrNull(other: Expression): Expression?
    fun reduceOrNull(other: Expression): Expression? {
        assert(this.final && other.final)
        assert(!other.isZeroRational())
        if (other.isUnitRational() || this.isZeroRational()) return this

        val reduced = _reduceOrNull(other)
        return reduced?.simplify()?.applyLoadingDomainFrom(this, other)
    }
    fun reduce(other: Expression): Expression = reduceOrNull(other)!!

    open fun _rationalPart(): Rational = unit()
    fun rationalPart() = _rationalPart().applyLoadingDomainFrom(this)
    open fun _nonRationalPart(): Expression = this
    fun nonRationalPart() = _nonRationalPart().applyLoadingDomainFrom(this)

    open fun _numericalPart(): Expression = if (isNumber) this else unit()
    fun numericalPart() = _numericalPart().applyLoadingDomainFrom(this)
    open fun _nonNumericalPart(): Expression = if (isNumber) unit() else this
    fun nonNumericalPart() = _nonNumericalPart().applyLoadingDomainFrom(this)

    override fun equals(other: Any?): Boolean {
        if (other == null) return false
        if (other::class != this::class) return false
        other as Expression
        return this.body == other.body
    }
    override fun hashCode() = body.hashCode()

    override fun compareTo(other: Expression): Int {
        val typeComparisonCode = compareExpressionTypes(this, other)
        return if (typeComparisonCode != 0) typeComparisonCode
          else                              this.toString() compareTo other.toString()
    }

    protected open fun _unaryMinus(): Expression = Product(-unit(), this)
    open operator fun unaryMinus() = _unaryMinus().applyLoadingDomainFrom(this)

    protected open fun _plus(other: Expression) = Sum(this, other)
    operator fun plus(other: Expression) = _plus(other).applyLoadingDomainFrom(this, other)

    protected open fun _minus(other: Expression) = Sum(this, -other)
    operator fun minus(other: Expression) = _minus(other).applyLoadingDomainFrom(this, other)

    protected open fun _times(other: Expression) = Product(this, other)
    operator fun times(other: Expression) = _times(other).applyLoadingDomainFrom(this, other)

    protected open fun _div(other: Expression) = Quotient(this to other)
    operator fun div(other: Expression) = _div(other).applyLoadingDomainFrom(this, other)

    protected open fun _raisedTo(other: Expression) = Power(this to other)
    infix fun raisedTo(other: Expression) = _raisedTo(other).applyLoadingDomainFrom(this, other)

    abstract override fun toString(): String
}


fun Expression.isUnitRational(): Boolean {
    contract { returns(true) implies(this@isUnitRational is Rational) }
    return this is Rational && this.isUnit()
}
fun Expression.isZeroRational(): Boolean {
    contract { returns(true) implies(this@isZeroRational is Rational) }
    return this is Rational && this.isZero()
}

fun Expression.asProduct() = this as? Product ?: Product(this)
fun Expression.asSum() = this as? Sum ?: Sum(this)

typealias ExpressionPair = Pair<Expression, Expression>
fun ExpressionPair.simplify(): ExpressionPair = first.simplify() to second.simplify()
