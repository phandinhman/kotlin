// WITH_RUNTIME

class ComparablePair<T : Comparable<T>>(val first: T, val second: T) : Comparable<ComparablePair<T>> {
    override fun compareTo(other: ComparablePair<T>): Int {
        val result = first.compareTo(other.first)
        return if (result != 0) result else second.compareTo(other.second)
    }
}

class MyComparableRange<T : Comparable<T>>(override val start: T, override val endInclusive: T) : ClosedRange<T>
operator fun Double.rangeTo(other: Double) = MyComparableRange(this, other)

fun check(x: Double, left: Double, right: Double): Boolean {
    val result = x in left..right
    val range = left..right
    assert(result == x in range) { "Failed: unoptimized === unoptimized" }
    return result
}

fun box(): String {
    assert("a" !in "b".."c")
    assert("b" in "a".."d")

    assert(ComparablePair(2, 2) !in ComparablePair(1, 10)..ComparablePair(2, 1))
    assert(ComparablePair(2, 2) in ComparablePair(2, 0)..ComparablePair(2, 10))

    assert(!check(-0.0, 0.0, 0.0))
    assert(check(Double.NaN, Double.NaN, Double.NaN))

    return "OK"
}
