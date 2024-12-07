fun main() {
    fun part1(input: List<String>): Int {
        val text = input.joinToString()
        return """mul\((\d{1,3}),(\d{1,3})\)"""
            .toRegex()
            .findAll(text)
            .sumOf { matchResult ->
                val (x, y) = matchResult.destructured
                x.toInt() * y.toInt()
            }
    }

    fun part2(input: List<String>): Int {
        val text = input.joinToString()
        var enable = true
        return """mul\((\d{1,3}),(\d{1,3})\)|do\(\)|don't\(\)"""
            .toRegex()
            .findAll(text)
            .sumOf { matchResult ->
                if (matchResult.value == "do()") {
                    enable = true
                    0
                } else if (matchResult.value == "don't()") {
                    enable = false
                    0
                } else {
                    val (x, y) = matchResult.destructured
                    if (enable) {
                        x.toInt() * y.toInt()
                    } else {
                        0
                    }
                }
            }
    }

    // Read the input from the `src/Day03.txt` file.
    val input = readInput("Day03")
    part1(input).println()
    part2(input).println()
}
