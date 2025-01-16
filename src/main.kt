import expressions.monomials.*

//import expressions.*

fun main() {
    val str = "b*2*3*5*c*d*c"
    println(str.split(' '))
    val m = str.toMonomial()
    println(m.body.first.numerator)
}
