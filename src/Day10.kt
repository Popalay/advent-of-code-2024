import java.util.LinkedList
import java.util.Queue

fun main() {
    fun part1(input: List<String>): Int = calculateScore(input, allowDifferentPaths = false)

    fun part2(input: List<String>): Int = calculateScore(input, allowDifferentPaths = true)

    // Read the input from the `src/Day10.txt` file.
    val input = readInput("Day10")
    part1(input).println()
    part2(input).println()
}

private fun calculateScore(
    input: List<String>,
    allowDifferentPaths: Boolean,
): Int {
    val map = input.map { it.map(Char::digitToInt) }
    val allStarts = mutableListOf<Point>()
    val allEnds = mutableListOf<Point>()

    map.forEachIndexed { y, row ->
        row.forEachIndexed { x, cell ->
            if (cell == 0) {
                allStarts.add(Point(x, y))
            } else if (cell == 9) {
                allEnds.add(Point(x, y))
            }
        }
    }

    return allStarts
        .flatMap { start -> allEnds.map { end -> start to end } }
        .sumOf { (start, end) -> map.isAccessible(start, end, allowDifferentPaths) }
}

/**
 * Returns the number of accessible paths from the start to the end.
 * @param allowDifferentPaths If `true`, the function will count the number of different paths.
 * Otherwise, it will stop when the first path is found.
 */
private fun List<List<Int>>.isAccessible(
    start: Point,
    end: Point,
    allowDifferentPaths: Boolean,
): Int {
    val queue: Queue<Point> = LinkedList()
    val visited = mutableSetOf<Point>()
    var trailsCount = 0

    queue.add(start)
    visited.add(start)

    while (queue.isNotEmpty()) {
        val current = queue.poll()

        if (current == end) trailsCount += 1

        for (direction in Direction.entries) {
            val next = current + direction.div

            if (this[next] == this[current]?.plus(1) && (allowDifferentPaths || next !in visited)) {
                queue.add(next)
                visited.add(next)
            }
        }
    }

    return trailsCount
}
