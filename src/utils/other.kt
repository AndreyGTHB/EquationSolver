package utils


inline fun <T> Iterable<T>.forEachExcept(index: Int, action: (T) -> Unit) {
    forEachIndexed { i, el -> if (i != index) action(el) }
}

inline fun <T> Iterable<T>.allIndexed(predicate: (Int, T) -> Boolean): Boolean {
    forEachIndexed { i, el -> if (!predicate(i, el)) return false }
    return true
}

inline fun <T> Iterable<T>.allExcept(index: Int, predicate: (T) -> Boolean): Boolean {
    return allIndexed { i, el -> i == index || predicate(el) }
}

inline fun <T> Iterable<T>.anyIndexed(predicate: (Int, T) -> Boolean): Boolean {
    forEachIndexed { i, el -> if (predicate(i, el)) return true }
    return false
}


fun Map<*, *>.firstValue() = values.first()

inline fun <R, K, V> Map<K, V>.fold(initial: R, action: (acc: R, Map.Entry<K, V>) -> R): R {
    var accumulator = initial
    for (element in this) accumulator = action(accumulator, element)
    return accumulator
}


inline fun <T> MutableList<T>.replaceAllIndexed(transform: (Int, T) -> T) {
    for ((i, element) in withIndex()) set(i, transform(i, element))
}


inline fun <T, R> Pair<T, T>.map(transform: (T) -> R) = transform(first) to transform(second)

fun <T : Comparable<T>> Pair<T, T>.sorted() = if (first > second) second to first else this
