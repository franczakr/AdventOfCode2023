package day6

import util.readLines
import util.removePrefixTrim
import java.util.stream.LongStream.rangeClosed

fun main() {
    val testLines = readLines("input_test.txt")
    val lines = readLines("input.txt")

    println("Part 1")
    part1(testLines).also { println(it) }.also { require(it == 288L) }
    part1(lines).also { println(it) }

    println()

    println("Part 2")
    part2(testLines).also { println(it) }.also { require(it == 71503L) }
    part2(lines).also { println(it) }
}

private fun part1(lines: List<String>): Long {
    val times = lines[0].removePrefixTrim("Time:").split(" +".toRegex()).map { it.trim().toLong() }
    val distances = lines[1].removePrefixTrim("Distance:").split(" +".toRegex()).map { it.trim().toLong() }

    val races = times.zip(distances).map { Race(it.first, it.second) }

    return races.map { race ->
        rangeClosed(0, race.time)
            .boxed()
            .map { Simulation(race, it, calculateDistance(race.time, it)) }
            .filter { simulation -> simulation.distance > simulation.race.recordDistance }
            .count()
    }.reduce(Long::times)
}

fun calculateDistance(totalTime: Long, chargingTime: Long): Long {
    return chargingTime * (totalTime - chargingTime)
}

data class Race(val time: Long, val recordDistance: Long)

data class Simulation(val race: Race, val chargingTime: Long, val distance: Long)

private fun part2(lines: List<String>): Long {
    val time = lines[0].removePrefixTrim("Time:").replace(" ", "").toLong()
    val recordDistance = lines[1].removePrefixTrim("Distance:").replace(" ", "").toLong()

    val race = Race(time, recordDistance)

    return rangeClosed(0, race.time)
        .boxed()
        .parallel()
        .map { Simulation(race, it, calculateDistance(race.time, it)) }
        .filter { simulation -> simulation.distance > simulation.race.recordDistance }
        .count()
}
