fun main() {
    fun part1(input: List<String>): Int {
        val (rulesInput, updatesInput) = input.filter(String::isNotBlank).partition { '|' in it }
        val rules = rulesInput.map { it.split("|").map(String::toInt) }.groupBy({ it.first() }, { it.last() })
        val updates = updatesInput.map { it.split(",").map(String::toInt) }

        val result =
            updates.sumOf {
                if (it.isCorrect(rules)) {
                    it.middle
                } else {
                    0
                }
            }

        return result
    }

    fun part2(input: List<String>): Int {
        val (rulesInput, updatesInput) = input.filter(String::isNotBlank).partition { '|' in it }
        val rules = rulesInput.map { it.split("|").map(String::toInt) }.groupBy({ it.first() }, { it.last() })
        val updates = updatesInput.map { it.split(",").map(String::toInt) }

        val incorrectUpdates = updates.filterNot { it.isCorrect(rules) }

        val result =
            incorrectUpdates
                .map { it.correctBy(rules) }
                .sumOf { it.middle }
        return result
    }

    // Read the input from the `src/Day05.txt` file.
    val input = readInput("Day05")
    part1(input).println()
    part2(input).println()
}

fun List<Int>.isCorrect(rules: Map<Int, List<Int>>): Boolean {
    val reversed = this.reversed()
    reversed.forEachIndexed { index, number ->
        val currentRule = rules[number] ?: emptyList()
        if (reversed.drop(index + 1).any { it in currentRule }) {
            return false
        }
    }
    return true
}

fun List<Int>.correctBy(rules: Map<Int, List<Int>>): List<Int> {
    val reversed = this.reversed()
    val result = reversed.toMutableList()
    var isCorrect = false
    var index = 0
    while (!isCorrect) {
        val current = result[index]
        val currentRule = rules[current] ?: emptyList()
        if (result.drop(index + 1).any { it in currentRule }) {
            // we should shift this element to the right
            result.removeAt(index)
            result.add(index + 1, current)
        }
        index++
        if (index == result.size) {
            index = 0
        }
        isCorrect = result.reversed().isCorrect(rules)
    }
    return result.reversed()
}

val List<Int>.middle: Int
    get() = this[this.size / 2]
