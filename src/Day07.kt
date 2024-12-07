import kotlin.math.pow

fun main() {
    fun part1(input: List<String>): Long = input.solveWith(setOf("*", "+"))

    fun part2(input: List<String>): Long = input.solveWith(setOf("*", "+", "||"))

    // Read the input from the `src/Day07.txt` file.
    val input = readInput("Day07")
    part1(input).println()
    part2(input).println()
}

private fun List<String>.solveWith(possibleOperators: Set<String>): Long {
    val equations = map { line -> line.split(": ").let { it.first().toLong() to it.last().split(" ").map { it.toLong() } } }

    return equations.sumOf { (expected, parts) ->
        if (parts.isSolvable(expected, possibleOperators)) expected else 0L
    }
}

private fun List<Long>.isSolvable(
    expected: Long,
    possibleOperators: Set<String>,
): Boolean {
    val combinationSize = this.size - 1
    val totalCombinations =
        possibleOperators.size
            .toDouble()
            .pow(combinationSize.toDouble())
            .toInt()

    for (i in 0 until totalCombinations) {
        var number = i
        val combination =
            buildList(combinationSize) {
                repeat(combinationSize) {
                    this.add(possibleOperators.elementAt(number % possibleOperators.size))
                    number /= possibleOperators.size
                }
            }
        if (checkEquations(expected, combination.reversed())) {
            return true
        }
    }

    return false
}

private fun List<Long>.checkEquations(
    expected: Long,
    combination: List<String>,
): Boolean {
    val operators = combination.iterator()
    val actual =
        this
            .drop(1)
            .fold(this[0]) { acc, l ->
                when (val operator = operators.next()) {
                    "*" -> acc * l
                    "+" -> acc + l
                    "||" -> "$acc$l".toLong()
                    else -> error("Unknown operator: $operator")
                }
            }
    return actual == expected
}
