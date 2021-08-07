package com.example.fantasy

import com.example.fantasy.PokerCombination.*

open class ResultCardsInRow(private val cards: List<Card>) : Cards(cards) {
    private val minFace
        get() = cards.map { it.face.rankAceHigh }.minOrNull() ?: throw IllegalStateException("minFace must be > 0")

    private val maxFace
        get() = cards.map { it.face.rankAceHigh }.maxOrNull() ?: throw IllegalStateException("maxFace must be > 0")

    val numberOfFaces
        get() = cards.map { it.face }
            .distinct()
            .count()

    val numberOfSuits
        get() = cards.map { it.suit }
            .distinct()
            .count()

    private val sortedCards = MutableCards(cards.toMutableList()).sortByValues()

//    fun isFlush() = numberOfSuits == 1
//    fun isStraight() = maxFace - minFace == cards.size - 1

    open fun pokerCombination(): PokerCombination {
        return when (numberOfFaces) {
            2 -> faces2()
            else -> throw IllegalStateException("Number of faces (must be 1, 2, or 3): $numberOfFaces")
        }
    }

    fun faces2(): PokerCombination {
//        when (sortedCards)
        return TRIPS
    }
}

class BottomRowCards(private val cards: List<Card>) : ResultCardsInRow(cards) {
    init {
        if (cards.size != 5) throw IllegalStateException("Number of bottom row cards (must be 5): ${cards.size}")
    }

    fun value(): Int {
        return when (pokerCombination()) {
            HIGH_CARD -> 0
            PAIR -> 0
            TWO_PAIRS -> 0
            TRIPS -> 0
            STRAIGHT -> 2
            FLUSH -> 4
            FULL_HOUSE -> 6
            QUADS -> 10
            STRAIGHT_FLUSH -> 15
            ROYAL_FLUSH -> 25
        }
    }
}

class MiddleRowCards(private val cards: List<Card>) : ResultCardsInRow(cards) {
    init {
        if (cards.size != 5) throw IllegalStateException("Number of middle row cards (must be 5): ${cards.size}")
    }

    fun value(): Int {
        return when (pokerCombination()) {
            HIGH_CARD -> 0
            PAIR -> 0
            TWO_PAIRS -> 0
            TRIPS -> 2
            STRAIGHT -> 4
            FLUSH -> 8
            FULL_HOUSE -> 12
            QUADS -> 20
            STRAIGHT_FLUSH -> 30
            ROYAL_FLUSH -> 50
        }
    }
}

class TopRowCards(private val cards: List<Card>) : ResultCardsInRow(cards) {
    init {
        if (cards.size != 3) throw IllegalStateException("Number of top row cards (must be 3): ${cards.size}")
    }

    override fun pokerCombination(): PokerCombination {
        return when (numberOfFaces) {
            1 -> TRIPS
            2 -> PAIR
            3 -> HIGH_CARD
            else -> throw IllegalStateException("Number of faces (must be 1, 2, or 3): $numberOfFaces")
        }
    }

    fun value(): Int {
        return when (pokerCombination()) {
            HIGH_CARD -> 0
            PAIR -> topRowPair()
            TRIPS -> topRowTrips()
            else -> throw IllegalStateException("Poker combination out of range for top row")
        }
    }

    private fun topRowPair(): Int {
        return 9
    }

    private fun topRowTrips(): Int {
        return 22
    }
}