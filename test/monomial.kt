import utils.toMonomial

fun main() {
    val m1 = "32*a*d*d*n*n*n*c".toMonomial()
    val m2 = "32*d*a*d*n*n*c*n".toMonomial()
    println(m1)
    println(m2)
    println(m1*m2)
    println((m1 / m2).simplify())
}