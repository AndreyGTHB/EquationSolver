package expressions

abstract class Expression {
    abstract val body: Any

    abstract fun simplified(): Expression
    abstract fun simplifiedSoftly(): Expression

//    open operator fun plus(exp: Expression): Sum {
//        return Sum(this to exp)
//    }
//    operator fun minus(exp: Expression): Difference {
//        return Difference(this to exp)
//    }
////    operator fun times(exp: Expression): Product {
////        return Product(this to exp)
////    }
//    operator fun div(exp: Expression): Quotient {
//        return Quotient(this to exp)
//    }
}