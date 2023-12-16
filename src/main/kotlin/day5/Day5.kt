package day5

import util.readText
import util.removePrefixTrim
import java.util.stream.LongStream.range

fun main() {
    val testInput = readText("input_test.txt")
    val input = readText("input.txt")

    println("Part 1")
    part1(testInput).also { println(it) }
        .also { require(it == 35L) }
    part1(input).also { println(it) }

    println()

    println("Part 2")
    part2(testInput).also { println(it) }.also { require(it == 46L) }
    part2(input).also { println(it) }
}

private fun part1(input: String): Long {

    val categories = input.split("\r\n\r\n")

    val seeds: List<Long> = categories[0].removePrefixTrim("seeds:").split(" ").map { it.toLong() }
    val categoryMappers: List<CategoryMapper> = categories.drop(1)
        .map { mapCategoryToCategoryMapper(it.split("\r\n")) }

    return seeds.minOf { seed ->
        categoryMappers.fold(seed) { acc, mapper -> mapper.map(acc) }
    }
}

private fun mapCategoryToCategoryMapper(it: List<String>): CategoryMapper {
    return it
        .drop(1)
        .filter { it.isNotBlank() }
        .map { line -> mapLineToMapper(line) }
        .let { CategoryMapper(it.toSet()) }
}

private fun mapLineToMapper(line: String): Mapper {
    return line
        .trim()
        .split(" ")
        .map { it.trim().toLong() }
        .let { Mapper(LongRange(it[1], it[1] + it[2])) { x -> x + it[0] - it[1] } }
}

data class Mapper(val sourceRange: LongRange, val mapperFun: (Long) -> Long)

data class CategoryMapper(val mappers: Set<Mapper>) {
    fun map(source: Long): Long {
        return mappers.firstOrNull { it.sourceRange.contains(source) }?.mapperFun?.invoke(source) ?: source
    }
}


private fun part2(input: String): Long {
    val categories = input.split("\r\n\r\n")

    val categoryMappers: List<CategoryMapper> = categories.drop(1)
        .map { mapCategoryToCategoryMapper(it.split("\r\n")) }

    return categories[0]
        .removePrefixTrim("seeds:")
        .let { ("(\\d+) (\\d+)".toRegex().findAll(it)) }
        .map {
            range(
                it.destructured.component1().toLong(),
                it.destructured.component1().toLong() + it.destructured.component2().toLong()
            )
        }
        .map { it.parallel().map { seed -> categoryMappers.fold(seed) { acc, mapper -> mapper.map(acc) } }.min() }
        .filter { it.isPresent }.map { it.asLong }
        .min()
}
