package day2

import util.readLines

fun main() {
    val testLines = readLines("input_test.txt")
    val lines = readLines("input.txt")

    println("Part 1")
    part1(testLines).also { println(it) }.also { require(it == 8) }
    part1(lines).also { println(it) }

    println()

    println("Part 2")
    part2(testLines).also { println(it) }.also { require(it == 2286) }
    part2(lines).also { println(it) }
}

private fun part1(lines: List<String>): Int {
    return lines
        .map(::toGame)
        .filter { it.isPossible() }
        .sumOf { it.id }
}

fun toGame(line: String): Game {
    val parts = line.split(":")
    val gameId = parts[0].filter { it.isDigit() }.toInt()
    val cubes = parts[1]
        .split(";")
        .map { set ->
            set.trim()
                .split(", ")
                .map { it.trim().split(" ").map(String::trim) }
                .groupBy({ Color.valueOf(it[1].uppercase()) }, { it[0].toInt() })
                .mapValues { entry -> entry.value.sum() }
        }
    return Game(gameId, cubes)
}

enum class Color(val max: Int) { BLUE(14), RED(12), GREEN(13) }
data class Game(val id: Int, val cubes: List<Map<Color, Int>>) {
    fun isPossible(): Boolean {
        return cubes.all { it.entries.all { entry -> entry.key.max >= entry.value } }
    }
}


private fun part2(lines: List<String>): Int {
    return lines
        .map(::toGame)
        .map(::leastPossibleCubes)
        .sumOf { it.values.reduce { a, b -> a * b } }
}

fun leastPossibleCubes(game: Game): Map<Color, Int> {
    return game.cubes.reduce { first, second ->
        (first.asSequence() + second.asSequence())
            .groupBy({ it.key }, { it.value })
            .mapValues { it.value.max() }
    }
}