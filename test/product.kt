import expressions.longs.Product
import expressions.longs.Sum
import expressions.numerical.Fraction
import utils.toMonomial

fun main() {
    val body1 = listOf(Fraction(1 to 10), Fraction(2 to 5), "n*e*e*a".toMonomial())
    val body2 = listOf(Sum(listOf(Fraction(5 to 2), "a".toMonomial())), Sum(listOf(Fraction(5 to 5), "b".toMonomial())))
    val pr1 = Product(body1)
    val pr2 = Product(body2)

    print(pr1.simplify())
    println(pr2)
    println(pr2.simplify())
}