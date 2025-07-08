package utils

inline fun <R, K, V> Map<K, V>.fold(initial: R, action: (acc: R, Map.Entry<K, V>) -> R): R {
    var accumulator = initial
    for (element in this) accumulator = action(accumulator, element)
    return accumulator
}

inline fun <T> MutableList<T>.replaceAllIndexed(transform: (Int, T) -> T) {
    for ((i, element) in withIndex()) set(i, transform(i, element))
}
