package equations

@Suppress("FunctionName")
abstract class Domain (final: Boolean) {
    var final = final
        private set

    // Intersection
    abstract operator fun times(other: Domain): Domain

    protected abstract fun _solve(): Domain
    fun solve(): Domain {
        return if (final) this
        else              _solve().apply { final = true }
    }
}