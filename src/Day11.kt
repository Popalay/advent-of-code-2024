import kotlin.math.pow

fun main() {
    fun part1(input: List<String>): Long {
        val stones = input.map { it.split(" ").map(String::toLong) }.flatten()

        return processStones(stones, 25)
    }

    fun part2(input: List<String>): Long {
        val stones = input.map { it.split(" ").map(String::toLong) }.flatten()

        return processStones(stones, 75)
    }

    // Read the input from the `src/Day11.txt` file.
    val input = readInput("Day11")
    part1(input).println()
    part2(input).println()
}

// AI helped me with this solution because I was stuck on the recursive part.
fun processStones(
    stones: List<Long>,
    iterations: Int,
): Long {
    // Initialize the counter with the initial stones
    var counter =
        stones
            .groupingBy { it }
            .eachCount()
            .mapValues { it.value.toLong() }
            .toMutableMap()

    repeat(iterations) {
        // Local counter to store the iteration counters
        val localCounter = mutableMapOf<Long, Long>()

        fun merge(
            stone: Long,
            count: Long,
        ) {
            // Merge the local counter with the global counter
            localCounter.merge(stone, count, Long::plus)
        }

        counter.forEach { (stone, count) ->
            when {
                stone == 0L -> merge(1, count)
                isEvenDigitCount(stone) -> {
                    val numDigits = digitCount(stone)
                    val halfLength = numDigits / 2
                    val divisor = 10.0.pow(halfLength.toDouble()).toLong()
                    val left = stone / divisor
                    val right = stone % divisor
                    merge(left, count)
                    merge(right, count)
                }

                else -> merge(stone * 2024, count)
            }
        }
        // Update the global counter with the local counter
        counter = localCounter.toMutableMap()
    }

    return counter.values.sum()
}

private fun isEvenDigitCount(num: Long): Boolean = digitCount(num) % 2 == 0
