package equations

object FullDomain : Domain(true) {
    override fun times(other: Domain) = other

    override fun _solve() = this
}