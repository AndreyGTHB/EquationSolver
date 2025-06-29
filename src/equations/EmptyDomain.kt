package equations

object EmptyDomain : Domain(true) {
    override fun times(other: Domain) = this

    override fun _solve() = this
}