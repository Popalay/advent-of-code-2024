fun main() {
    fun part1(input: List<String>): Int {
        val map = input.map { it.toCharArray() }

        return map.findAntiNodes(withResonance = false)
            .filter { it.y in map.indices && it.x in map[0].indices }
            .size
    }

    fun part2(input: List<String>): Int {
        val map = input.map { it.toCharArray() }

        return map.findAntiNodes(withResonance = true)
            .filter { it.y in map.indices && it.x in map[0].indices }
            .size
    }

    // Read the input from the `src/Day08.txt` file.
    val input = readInput("Day08")
    part1(input).println()
    part2(input).println()
}

private fun List<CharArray>.findAntiNodes(withResonance: Boolean): Set<Point> {
    val antennas = mutableListOf<Pair<Char, Point>>()

    this.forEachIndexed { y, row ->
        row.forEachIndexed { x, cell ->
            if (cell != '.') {
                antennas.add(cell to Point(x, y))
            }
        }
    }

    val antiNodes = mutableSetOf<Point>()
    val antennasMap = antennas.groupBy({ it.first }, { it.second })

    antennasMap.forEach { antenna ->
        antenna.value.forEach { point1 ->
            antenna.value.forEach { point2 ->
                antiNodes.addAll(findAntiNodesFor(point1, point2, withResonance))
            }
        }
    }

    return antiNodes
}

private fun findAntiNodesFor(point1: Point, point2: Point, withResonance: Boolean): Set<Point> {
    if (point1 == point2) return emptySet()
    val repeatNumber = if(withResonance) 50 else 1

    val result = mutableSetOf<Point>()
    val delta = point1 - point2

    if (point1 + delta == point2) {
        var newPoint1 = point1
        repeat(repeatNumber) {
            newPoint1 -= delta
            result.add(newPoint1)
        }
    } else {
        var newPoint1 = point1
        repeat(repeatNumber) {
            newPoint1 += delta
            result.add(newPoint1)
        }
    }

    if (point2 + delta == point1) {
        var newPoint2 = point2
        repeat(repeatNumber) {
            newPoint2 -= delta
            result.add(newPoint2)
        }
    } else {
        var newPoint2 = point2
        repeat(repeatNumber) {
            newPoint2 += delta
            result.add(newPoint2)
        }
    }

    if(withResonance) {
        result.add(point1)
        result.add(point2)
    }

    return result
}