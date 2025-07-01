package parser

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class SubStringsTest {
    @ParameterizedTest
    @ValueSource(strings = ["abc", "123456789", ".", ""])
    fun `Standard substrings`(str: String) {
        assertThrows(AssertionError::class.java, { str.subStrings(0) })

        for (currLength in 1..str.length) {
            str.subStrings(currLength).forEachIndexed { i, subStr ->
                assertEquals(str.slice(i ..< i+currLength), subStr)
            }
        }
    }
}