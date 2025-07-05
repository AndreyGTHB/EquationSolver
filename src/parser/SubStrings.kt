package parser

class SubStringsIterator(val str: String, val length: Int) : Iterator<String> {
    private var i = 0

    init { assert(length > 0) }

    override fun hasNext() = i + length <= str.length

    override fun next(): String {
        val next = str.slice(i ..< i+length)
        i++
        return next
    }
}


fun String.subStrings(length: Int) = object : Iterable<String> {
        override fun iterator() = SubStringsIterator(this@subStrings, length)
}