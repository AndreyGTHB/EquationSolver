import expressions.longs.Product
import expressions.longs.Sum
import expressions.number.Rational
import expressions.unit
import utils.over
import utils.toMonomial
import utils.toRational

fun main() {
    val body1 = listOf(Rational(1 to 10), Rational(2 to 5), "n*e*e*a".toMonomial())
    val body2 = listOf(Sum(listOf(Rational(5 to 2), "a".toMonomial())), Sum(listOf(Rational(5 to 5), "b".toMonomial())))
//    val pr1 = Product(body1)
    val pr2 = Product(body2)

//    print(pr1.simplify())
//    println(pr2)
//    println(pr2.simplify())

    val emptyProduct = Product(listOf())
    println(emptyProduct)
    println(emptyProduct.simplify())

    val pr3 = Product(listOf(-unit(), 3.toRational(), -unit() * (5 over 9)))
    println(pr3.simplify())
}