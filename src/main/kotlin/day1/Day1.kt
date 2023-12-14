package day1

import util.readLines

fun main() {
    val testLines = readLines("input_test.txt")
    val lines = readLines("input.txt")

    println("Part 1")
    part1(testLines).also { println(it) }.also { require(it == 142) }
    part1(lines).also { println(it) }

    println()

    val testLines2 = readLines("input_test2.txt")

    println("Part 2")
    part2(testLines2).also { println(it) }.also { require(it == 281) }
    part2(lines).also { println(it) }
}

private fun part1(lines: List<String>) = lines.sumOf { line ->
    val firstDigit = line.first { it.isDigit() }.digitToInt()
    val lastDigit = line.last { it.isDigit() }.digitToInt()
    10 * firstDigit + lastDigit
}

private fun part2(lines: List<String>) = lines.sumOf { line ->
    val firstDigit = firstDigit(line)
    val lastDigit = lastDigit(line)
    (10 * firstDigit + lastDigit)
}

private fun firstDigit(line: String): Int {
    val start = line.takeWhile { !it.isDigit() }
    for (i in 0..start.lastIndex) {
        val substring = start.substring(i)
        parseDigit(substring).also { if(it != null) return it }
    }
    return line.first { it.isDigit() }.digitToInt()
}

private fun lastDigit(line: String): Int {
    val end = line.takeLastWhile { !it.isDigit() }
    for (i in (0..end.lastIndex).reversed()) {
        val substring = end.substring(i)
        parseDigit(substring).also { if(it != null) return it }
    }
    return line.last { it.isDigit() }.digitToInt()
}

private fun parseDigit(substring: String): Int? {
    if (substring.startsWith("one")) return 1
    if (substring.startsWith("two")) return 2
    if (substring.startsWith("three")) return 3
    if (substring.startsWith("four")) return 4
    if (substring.startsWith("five")) return 5
    if (substring.startsWith("six")) return 6
    if (substring.startsWith("seven")) return 7
    if (substring.startsWith("eight")) return 8
    if (substring.startsWith("nine")) return 9
    return null
}


