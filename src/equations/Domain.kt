package equations

class Domain(val restrictions: MutableList<Equation> = mutableListOf()) {
    constructor(restrictions: List<Equation>) : this(restrictions.toMutableList())
    constructor(vararg restrictions: Equation) : this(restrictions.toMutableList())

    fun simplify() { TODO("Solving and uniting all the restrictions") }

    operator fun plus(other: Equation) = Domain((restrictions + other).toMutableList())
    operator fun plusAssign(other: Equation) { restrictions += other }

    operator fun plus(other: Domain) = Domain((restrictions + other.restrictions).toMutableList())
    operator fun plusAssign(other: Domain) { restrictions += other.restrictions }

    override fun toString(): String {
        var asString = "RAV:\n"
        restrictions.forEach { subExp ->
            val subExpStrs = subExp.toString().split("\n")
            subExpStrs.forEach { subExpStr -> asString += "  $subExpStr\n" }
        }
        return asString
    }
}