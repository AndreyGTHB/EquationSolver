package equations

class RestrictingDomain(val restrictions: Set<Condition>, final: Boolean = false) : Domain(final) {
    constructor(vararg restrictions: Condition) : this(restrictions.toMutableSet())

    override fun times(other: Domain): Domain {
        return when (other) {
            is EmptyDomain -> other
            is FullDomain -> this
            is RestrictingDomain -> RestrictingDomain(this.restrictions.union(other.restrictions))
            else -> TODO()
        }
    }

    override fun _solve(): Domain { TODO() }

    override fun toString(): String {
        var asString = "${this::class.simpleName}:\n"
        restrictions.forEach {
            val subExpStrs = it.toString().split("\n")
            subExpStrs.forEach { subExpStr -> asString += "  $subExpStr\n" }
        }
        return asString
    }
}
