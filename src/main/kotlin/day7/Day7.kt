package day7

import day7.HandType.*
import util.readLines

fun main() {
    val testLines = readLines("input_test.txt")
    val lines = readLines("input.txt")

    println("Part 1")
    part1(testLines).also { println(it) }.also { require(it == 6440L) }
    part1(lines).also { println(it) }

    println()

    println("Part 2")
    part2(testLines).also { println(it) }.also { require(it == 5905L) }
    part2(lines).also { println(it) }
}

data class CardPart1(val symbol: Char) : Comparable<CardPart1> {

    override fun compareTo(other: CardPart1): Int {
        if (symbol == other.symbol) return 0
        if (symbol.isDigit() && other.symbol.isDigit()) {
            return symbol.digitToInt().compareTo(other.symbol.digitToInt())
        }
        if (other.symbol.isDigit()) return 1
        if (symbol.isDigit()) return -1
        if (symbol == 'A') return 1
        if (symbol == 'K' && other.symbol != 'A') return 1
        if (symbol == 'Q' && !listOf('A', 'K').contains(other.symbol)) return 1
        if (symbol == 'J' && !listOf('A', 'K', 'Q').contains(other.symbol)) return 1
        return -1
    }

    override fun toString(): String {
        return symbol.toString()
    }
}

enum class HandType {
    HIGH_CARD, PAIR, TWO_PAIR, THREE, FULL, FOUR, FIVE
}


data class HandPart1(val cards: List<CardPart1>, val type: HandType) : Comparable<HandPart1> {

    constructor(cards: List<CardPart1>) : this(cards, resolveType(cards))

    companion object {
        private fun resolveType(cards: List<CardPart1>): HandType {
            val symbolsCounts = cards.groupingBy { it.symbol }.eachCount()
            return when (symbolsCounts.size) {
                1 -> FIVE
                2 -> if (symbolsCounts.containsValue(4)) FOUR else FULL
                3 -> if (symbolsCounts.containsValue(3)) THREE else TWO_PAIR
                4 -> PAIR
                5 -> HIGH_CARD
                else -> throw IllegalStateException("Cant be more than 5 cards")
            }
        }
    }

    override fun compareTo(other: HandPart1): Int {
        return if (type.compareTo(other.type) != 0)
            type.compareTo(other.type)
        else
            compareCards(cards, other.cards)
    }

    private fun compareCards(cards: List<CardPart1>, otherCards: List<CardPart1>): Int {
        for (i in 0..cards.lastIndex) {
            if (cards[i].compareTo(otherCards[i]) != 0) return cards[i].compareTo(otherCards[i])
        }
        return 0
    }
}

data class GamePart1(val hand: HandPart1, val bid: Int)

private fun part1(lines: List<String>): Long {
    return lines
        .map { line ->
            line.split(" ")
                .let {
                    GamePart1(
                        it[0].trim().map { char -> CardPart1(char) }.let { cards -> HandPart1(cards) },
                        it[1].trim().toInt()
                    )
                }
        }
        .sortedBy { it.hand }
        .mapIndexed { index, game -> (index + 1) * game.bid.toLong() }
        .sum()
}


data class CardPart2(val symbol: Char) : Comparable<CardPart2> {

    override fun compareTo(other: CardPart2): Int {
        if (symbol == other.symbol) return 0
        if (symbol == 'J') return -1
        if (other.symbol == 'J') return 1
        if (symbol.isDigit() && other.symbol.isDigit()) {
            return symbol.digitToInt().compareTo(other.symbol.digitToInt())
        }
        if (other.symbol.isDigit()) return 1
        if (symbol.isDigit()) return -1
        if (symbol == 'A') return 1
        if (symbol == 'K' && other.symbol != 'A') return 1
        if (symbol == 'Q' && !listOf('A', 'K').contains(other.symbol)) return 1
        return -1
    }

    override fun toString(): String {
        return symbol.toString()
    }
}

data class HandPart2(val cards: List<CardPart2>, val type: HandType) : Comparable<HandPart2> {

    constructor(cards: List<CardPart2>) : this(cards, resolveTypeWithJokers(cards))

    companion object {
        private fun resolveType(cards: List<CardPart2>): HandType {
            val symbolsCounts = cards.groupingBy { it.symbol }.eachCount()
            return when (symbolsCounts.size) {
                1 -> FIVE
                2 -> if (symbolsCounts.containsValue(4)) FOUR else FULL
                3 -> if (symbolsCounts.containsValue(3)) THREE else TWO_PAIR
                4 -> PAIR
                5 -> HIGH_CARD
                else -> throw IllegalStateException("Cant be more than 5 cards")
            }
        }

        private fun resolveTypeWithJokers(cards: List<CardPart2>): HandType {
            if (cards.all { it == cards[0] }) return resolveType(cards)
            val mostCommonSymbol =
                cards.groupingBy { it.symbol }.eachCount().filterKeys { it != 'J' }.maxBy { it.value }.key
            return resolveType(cards.map { if (it.symbol == 'J') CardPart2(mostCommonSymbol) else it })
        }
    }

    override fun compareTo(other: HandPart2): Int {
        return if (type.compareTo(other.type) != 0)
            type.compareTo(other.type)
        else
            compareCards(cards, other.cards)
    }

    private fun compareCards(cards: List<CardPart2>, otherCards: List<CardPart2>): Int {
        for (i in 0..cards.lastIndex) {
            if (cards[i].compareTo(otherCards[i]) != 0) return cards[i].compareTo(otherCards[i])
        }
        return 0
    }
}

data class GamePart2(val hand: HandPart2, val bid: Int)

private fun part2(lines: List<String>): Long {
    return lines
        .map { line ->
            line.split(" ")
                .let {
                    GamePart2(
                        it[0].trim().map { char -> CardPart2(char) }.let { cards -> HandPart2(cards) },
                        it[1].trim().toInt()
                    )
                }
        }
        .sortedBy { it.hand }
        .mapIndexed { index, game -> (index + 1) * game.bid.toLong() }
        .sum()
}
