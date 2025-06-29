package equations

class PermittingDomain (val permissions: Set<Condition>, final: Boolean = false) : Domain(final) {
    constructor(vararg permissions: Condition) : this(permissions.toSet())

    override fun times(other: Domain): Domain {
        return when (other) {
            is EmptyDomain -> other
            is FullDomain -> this
            is PermittingDomain -> PermittingDomain(this.permissions.intersect(other.permissions))
            is Condition -> PermittingDomain(permissions + other)
            else -> TODO()
        }
    }

    override fun _solve(): Domain {
        val sDomains = permissions.map { it.solve() }
        val newPermissions = mutableSetOf<Condition>()
        sDomains.forEach { subDomain ->
            when (subDomain) {
                is EmptyDomain      -> return subDomain
                is Condition        -> newPermissions.add(subDomain)
                is PermittingDomain -> subDomain.permissions.forEach { newPermissions.add(it) }
                else                -> TODO()
            }
        }

        return when (newPermissions.size) {
            0    -> EmptyDomain
            1    -> newPermissions.first()
            else -> PermittingDomain(newPermissions.toSortedSet())
        }
    }
}