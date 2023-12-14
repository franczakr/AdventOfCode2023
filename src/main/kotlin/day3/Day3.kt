package day3

import util.readLines
import java.util.stream.IntStream.rangeClosed

fun main() {
    val testLines = readLines("input_test.txt")
    val lines = readLines("input.txt")

    println("Part 1")
    part1(testLines).also { println(it) }.also { require(it == 4361) }
    part1(lines).also { println(it) }

    println()

    println("Part 2")
    part2(testLines).also { println(it) }.also { require(it == 467835) }
    part2(lines).also { println(it) }
}

private fun part1(lines: List<String>): Int {
    return lines.flatMapIndexed { lineNum, line -> parseNumbersWithPosition(line, lineNum) }
        .filter { isAdjacentToSymbol(it, lines) }
        .sumOf { it.number }
}

private fun parseNumbersWithPosition(
    line: String,
    lineNum: Int
): List<NumberWithPosition> {
    val numbers = mutableListOf<NumberWithPosition>()
    var i = 0
    while (i < line.length) {
        if (line[i].isDigit()) {
            val startPos = i
            var num = line[i].digitToInt()
            while (i + 1 <= line.lastIndex && line[i + 1].isDigit()) {
                num = num * 10 + line[i + 1].digitToInt()
                i++
            }
            val endPos = i
            numbers.add(NumberWithPosition(num, lineNum, startPos, endPos))
        }
        i++
    }
    return numbers
}

fun isAdjacentToSymbol(number: NumberWithPosition, lines: List<String>): Boolean =
    rangeClosed(number.lineNum - 1, number.lineNum + 1)
        .anyMatch { lineNum ->
            rangeClosed(number.startPos - 1, number.endPos + 1).anyMatch { position ->
                IntRange(0, lines.lastIndex).contains(lineNum)
                        && IntRange(0, lines[lineNum].lastIndex).contains(position)
                        && !lines[lineNum][position].isLetterOrDigit()
                        && lines[lineNum][position] != '.'
            }
        }

data class NumberWithPosition(val number: Int, val lineNum: Int, val startPos: Int, val endPos: Int)


private fun parseAsterisksPositions(
    line: String,
    lineNum: Int
): List<Pair<Int, Int>> {
    return line.mapIndexed { index, char -> Triple(char, lineNum, index) }
        .filter { it.first == '*' }
        .map { Pair(it.second, it.third) }
}


private fun part2(lines: List<String>): Int {
    val numbers = lines.flatMapIndexed { lineNum, line -> parseNumbersWithPosition(line, lineNum) }

    return lines.flatMapIndexed { lineNum, line -> parseAsterisksPositions(line, lineNum) }
        .map { getAdjacentNumbers(it, numbers) }
        .filter { it.size == 2 }
        .sumOf { it.reduce(Int::times) }
}

fun getAdjacentNumbers(position: Pair<Int, Int>, numbers: List<NumberWithPosition>): List<Int> {
    return numbers.filter { number ->
        IntRange(number.lineNum - 1, number.lineNum + 1).contains(position.first)
                && IntRange(number.startPos - 1, number.endPos + 1).contains(position.second)
    }.map { it.number }
}
