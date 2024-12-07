fun main() {
    // Predict the path of the guard.
    // How many distinct positions will the guard visit before leaving the mapped area?
    fun part1(input: List<String>): Int {
        val map = input.map { it.toCharArray() }
        val start = map.initialPosition

        return map.finPatch(start).first.size
    }

    // You need to get the guard stuck in a loop by adding a single new obstruction.
    // How many different positions could you choose for this obstruction?
    fun part2(input: List<String>): Int {
        val map = input.map { it.toCharArray() }
        val start = map.initialPosition

        return map
            .finPatch(start)
            .first
            .filterNot { it == start }
            .count { (x, y) ->
                map[y][x] = '#'
                val stuck = map.finPatch(start).second
                map[y][x] = '.' // Restore the map
                stuck
            }
    }

    // Read the input from the `src/Day06.txt` file.
    val input = readInput("Day06")
    part1(input).println()
    part2(input).println()
}

private val List<CharArray>.initialPosition
    get() =
        mapIndexed { i, row -> row.indexOfFirst { it !in "#." } to i }
            .first { it.first != -1 }
            .let { (x, y) -> x to y }

private fun List<CharArray>.finPatch(start: Pair<Int, Int>): Pair<Set<Pair<Int, Int>>, Boolean> {
    val seen = mutableSetOf<Pair<Pair<Int, Int>, Pair<Int, Int>>>() // position, direction
    var (x, y) = start

    var direction = this[y][x].getInitialDirection()
    while (y in this.indices && x in this[0].indices && (x to y) to direction !in seen) {
        seen.add((x to y) to direction)
        val nextX = x + direction.first
        val nextY = y + direction.second
        if (this.getOrNull(nextY)?.getOrNull(nextX) == '#') {
            direction = direction.nextDirection()
        } else {
            x += direction.first
            y += direction.second
        }
    }

    return seen.map { it.first }.toSet() to (this.getOrNull(y)?.getOrNull(x) != null)
}

private fun Pair<Int, Int>.nextDirection(): Pair<Int, Int> =
    when (this) {
        Pair(0, -1) -> Pair(1, 0)
        Pair(0, 1) -> Pair(-1, 0)
        Pair(-1, 0) -> Pair(0, -1)
        Pair(1, 0) -> Pair(0, 1)
        else -> throw IllegalArgumentException("Invalid direction: $this")
    }

private fun Char.getInitialDirection() =
    when (this) {
        '^' -> Pair(0, -1)
        'v' -> Pair(0, 1)
        '<' -> Pair(-1, 0)
        '>' -> Pair(1, 0)
        else -> throw IllegalArgumentException("Invalid char: $this")
    }
