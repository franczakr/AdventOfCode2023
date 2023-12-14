package template

import util.readLines

fun main() {
    val testLines = readLines("input_test.txt")
    val lines = readLines("input.txt")

    println("Part 1")
    part1(testLines).also { println(it) }.also { require(it == 1) }
    part1(lines).also { println(it) }

    println()

    println("Part 2")
    part2(testLines).also { println(it) }.also { require(it == 1) }
    part2(lines).also { println(it) }
}

private fun part1(lines: List<String>): Int {
    return 0
}


private fun part2(lines: List<String>): Int {
    return 0
}
