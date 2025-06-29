class A {
    val a = 0
    val b = '0'
    fun method(arg: B) { println("B arg") }
    fun method(arg: C) { println("C arg") }
}


open class B {
    val x = 100
}
class C : B() {
    val y = 200
}

fun main() {

}
