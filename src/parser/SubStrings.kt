package parser

class SubStringsIterator(val str: String, val subLength: Int) : Iterator<String> {
    private var i = 0

    init { assert(subLength > 0) }

    override fun hasNext() = i + subLength <= str.length

    override fun next(): String {
        val next = str.slice(i ..< i+subLength)
        i++
        return next
    }
}


fun String.subStrings(subLength: Int): Iterable<String> {
    assert(subLength > 0)
    return object : Iterable<String> {
        override fun iterator() = SubStringsIterator(this@subStrings, subLength)
    }
}
