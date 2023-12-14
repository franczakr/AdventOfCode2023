package day4

import util.readLines
import java.util.stream.IntStream.rangeClosed
import kotlin.math.pow

fun main() {
    val testLines = readLines("input_test.txt")
    val lines = readLines("input.txt")

    println("Part 1")
    part1(testLines).also { println(it) }.also { require(it == 13) }
    part1(lines).also { println(it) }

    println()

    println("Part 2")
    part2(testLines).also { println(it) }.also { require(it == 30) }
    part2(lines).also { println(it) }
}

private fun part1(lines: List<String>): Int {
    return lines
        .map { calculateCardScore(it) }
        .sumOf { if (it == 0) 0 else (2.0).pow(it - 1).toInt() }
}

private fun calculateCardScore(line: String): Int {
    return line.split(":")[1]
        .split("|")
        .map { it.trim() }
        .map { it.split(" +".toRegex()).map { num -> num.trim().toInt() } }
        .map { it.toSet() }
        .let { it[0].intersect(it[1]).size }
}


private fun part2(lines: List<String>): Int {
    val lineNumToGeneratedCopies: MutableMap<Int, Int> = mutableMapOf()

    fun calculateGeneratedCopies(lines: List<String>, lineNum: Int): Int {
        if (lineNumToGeneratedCopies.containsKey(lineNum)) {
            return lineNumToGeneratedCopies[lineNum]!!
        }
        return rangeClosed(lineNum + 1, lineNum + calculateCardScore(lines[lineNum]))
            .map { 1 + calculateGeneratedCopies(lines, it) }
            .sum()
            .also { lineNumToGeneratedCopies[lineNum] = it }
    }

    return List(lines.size) { index ->
        1 + calculateGeneratedCopies(lines, index)
    }.sum()
}