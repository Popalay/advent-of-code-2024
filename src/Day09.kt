fun main() {
    fun part1(input: List<String>): Long {
        val memory = buildMemory(input.first())
        val fragmentedMemory = deFragmentMemory(memory)

        return fragmentedMemory.checkSum()
    }

    fun part2(input: List<String>): Long {
        val memory = buildMemory(input.first())
        val fragmentedMemory = deFragmentMemoryOptimized(memory)

        return fragmentedMemory.checkSum()
    }

    // Read the input from the `src/Day09.txt` file.
    val input = readInput("Day09")
    part1(input).println()
    part2(input).println()
}

private fun buildMemory(input: String): String =
    buildString {
        input.forEachIndexed { index, c ->
            val value =
                if (index % 2 == 0) {
                    (index / 2).toString().padStart(4, '0')
                } else {
                    ".".padStart(4, '.')
                }
            repeat(c.digitToInt()) { append(value) }
        }
    }

private fun deFragmentMemory(memory: String): List<String> {
    val chunks = memory.chunked(4).toMutableList()

    while (true) {
        val freeIndex = chunks.indexOfFirst { it == "...." }
        val lastFileIndex = chunks.indexOfLast { it != "...." }

        if (freeIndex > lastFileIndex) {
            break
        }

        chunks[freeIndex] = chunks[lastFileIndex]
        chunks[lastFileIndex] = "...."
    }
    return chunks
}

private fun deFragmentMemoryOptimized(memory: String): List<String> {
    val chunks =
        memory
            .chunked(4)
            .reversed()
            .fold(mutableListOf<MutableList<String>>()) { acc, element ->
                if (acc.isEmpty() || acc.last().last() != element) {
                    acc.add(mutableListOf(element))
                } else {
                    acc.last().add(element)
                }
                acc
            }

    var checked = Int.MAX_VALUE
    while (true) {
        val suitableFileIndex =
            chunks
                .asSequence()
                .withIndex()
                .indexOfFirst { (index, it) ->
                    "...." !in it &&
                        it.any { it.toInt() < checked } &&
                        it.size <= (
                            chunks
                                .asSequence()
                                .drop(index + 1)
                                .filter { "...." in it }
                                .maxOfOrNull { it.size } ?: 0
                        )
                }

        if (suitableFileIndex == -1) {
            break
        }

        val suitableFileSize = chunks[suitableFileIndex].size
        val suitableFreeSpaceIndex =
            chunks.indexOfLast { "...." in it && it.size >= suitableFileSize }
        val suitableFreeSpaceSize = chunks[suitableFreeSpaceIndex].size

        checked = chunks[suitableFileIndex].first().toInt()

        chunks[suitableFreeSpaceIndex] = chunks[suitableFileIndex]
        chunks[suitableFileIndex] = chunks[suitableFileIndex].map { "...." }.toMutableList()
        if (suitableFileSize < suitableFreeSpaceSize) {
            chunks.add(
                suitableFreeSpaceIndex,
                buildList { repeat(suitableFreeSpaceSize - suitableFileSize) { add("....") } }.toMutableList(),
            )
        }
    }

    return chunks.reversed().flatten()
}

private fun List<String>.checkSum(): Long =
    this
        .map { it.toLongOrNull() ?: 0 }
        .mapIndexed { index, number -> index * number }
        .sum()
