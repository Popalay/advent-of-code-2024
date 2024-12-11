import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readText
import kotlin.math.log10

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = Path("src/$name.txt").readText().trim().lines()

/**
 * Converts string to md5 hash.
 */
fun String.md5() =
    BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
        .toString(16)
        .padStart(32, '0')

/**
 * The cleaner shorthand for printing output.
 */
fun Any?.println() = println(this)

data class Point(
    val x: Int,
    val y: Int,
) {
    operator fun minus(other: Point) = Point(x - other.x, y - other.y)

    operator fun plus(other: Point) = Point(x + other.x, y + other.y)
}

enum class Direction(
    val div: Point,
) {
    UP(Point(0, -1)),
    DOWN(Point(0, 1)),
    LEFT(Point(-1, 0)),
    RIGHT(Point(1, 0)),
}

operator fun List<List<Int>>.get(point: Point): Int? = this.getOrNull(point.y)?.getOrNull(point.x)

fun digitCount(num: Long): Int = if (num == 0L) 1 else log10(num.toDouble()).toInt() + 1
