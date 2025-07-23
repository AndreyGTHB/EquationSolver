package utils

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
