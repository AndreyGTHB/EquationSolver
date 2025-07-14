import expressions.longs.Product
import expressions.longs.Sum
import expressions.number.Rational
import expressions.number.over
import expressions.number.toRational
import expressions.one
import utils.toMonomial

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

    val pr3 = Product(listOf(-one(), 3.toRational(), -one() * (5 over 9)))
    println(pr3.simplify())
}