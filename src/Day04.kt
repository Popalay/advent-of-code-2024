fun main() {
    fun part1(input: List<String>): Int {
        val chars = input.map(String::toList)
        val patterns = listOf(
                listOf(listOf('X', 'M', 'A', 'S')),
                listOf(listOf('S', 'A', 'M', 'X')),
                listOf(
                        listOf('X'),
                        listOf('M'),
                        listOf('A'),
                        listOf('S'),
                ),
                listOf(
                        listOf('S'),
                        listOf('A'),
                        listOf('M'),
                        listOf('X'),
                ),
                listOf(
                        listOf('X', '•', '•', '•'),
                        listOf('•', 'M', '•', '•'),
                        listOf('•', '•', 'A', '•'),
                        listOf('•', '•', '•', 'S'),
                ),
                listOf(
                        listOf('•', '•', '•', 'X'),
                        listOf('•', '•', 'M', '•'),
                        listOf('•', 'A', '•', '•'),
                        listOf('S', '•', '•', '•'),
                ),
                listOf(
                        listOf('S', '•', '•', '•'),
                        listOf('•', 'A', '•', '•'),
                        listOf('•', '•', 'M', '•'),
                        listOf('•', '•', '•', 'X'),
                ),
                listOf(
                        listOf('•', '•', '•', 'S'),
                        listOf('•', '•', 'A', '•'),
                        listOf('•', 'M', '•', '•'),
                        listOf('X', '•', '•', '•'),
                ),
        )

        val edited = input.map { '•'.toString().repeat(it.length) }.toMutableList()
        var count = 0

        patterns.forEach {
            val matches = chars.findPattern(it)
            matches.forEach { (i, j) ->
                for (k in 0..it.lastIndex) {
                    for (l in 0..it[k].lastIndex) {
                        edited[i + k] = edited[i + k].replaceRange(j + l, j + l + 1, it[k][l].toString())
                    }
                }
            }
            count += matches.size
        }

        edited.forEach(::println)

        return count
    }

    fun part2(input: List<String>): Int {
        val chars = input.map(String::toList)
        val patterns = listOf(
                listOf(
                        listOf('M', '•', 'S'),
                        listOf('•', 'A', '•'),
                        listOf('M', '•', 'S'),
                ),
                listOf(
                        listOf('M', '•', 'M'),
                        listOf('•', 'A', '•'),
                        listOf('S', '•', 'S'),
                ),
                listOf(
                        listOf('S', '•', 'S'),
                        listOf('•', 'A', '•'),
                        listOf('M', '•', 'M'),
                ),
                listOf(
                        listOf('S', '•', 'M'),
                        listOf('•', 'A', '•'),
                        listOf('S', '•', 'M'),
                ),
        )

        val edited = input.map { '•'.toString().repeat(it.length) }.toMutableList()
        var count = 0

        patterns.forEach {
            val matches = chars.findPattern(it)
            matches.forEach { (i, j) ->
                for (k in 0..it.lastIndex) {
                    for (l in 0..it[k].lastIndex) {
                        edited[i + k] = edited[i + k].replaceRange(j + l, j + l + 1, it[k][l].toString())
                    }
                }
            }
            count += matches.size
        }

        edited.forEach(::println)

        return count
    }


    // Read the input from the `src/Day04.txt` file.
    val input = readInput("Day04")
    part1(input).println()
    part2(input).println()
}

fun List<List<Char>>.findPattern(pattern: List<List<Char>>): List<Pair<Int, Int>> {
    val gridRows = size
    val gridCols = this[0].size
    val patternRows = pattern.size
    val patternCols = pattern[0].size
    val result = mutableListOf<Pair<Int, Int>>()

    // Iterate over each possible starting position in the grid
    for (i in 0..(gridRows - patternRows)) {
        for (j in 0..(gridCols - patternCols)) {
            // Check if the pattern matches the subarray starting at (i, j)
            if (isMatch(pattern, i, j)) {
                result.add(i to j)
            }
        }
    }
    return result.toList()
}

fun List<List<Char>>.isMatch(pattern: List<List<Char>>, startRow: Int, startCol: Int): Boolean {
    val patternRows = pattern.size
    val patternCols = pattern[0].size

    for (i in 0 until patternRows) {
        for (j in 0 until patternCols) {
            if (this[startRow + i][startCol + j] != pattern[i][j] && pattern[i][j] != '•') {
                return false
            }
        }
    }
    return true
}