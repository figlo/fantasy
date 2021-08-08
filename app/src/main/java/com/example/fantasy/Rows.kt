package com.example.fantasy

import com.example.fantasy.CardFace.*
import com.example.fantasy.PokerCombination.*

open class RowCards(private val cards: List<Card>) : Cards(cards) {
    init {
        if (cards.size != 3 && cards.size != 5) throw IllegalStateException("Number of row cards (must be 3 or 5): ${cards.size}")
    }

    private val minFace
        get() = cards.map { it.face.rankAceHigh }.minOrNull() ?: throw IllegalStateException("minFace must be > 0")

    private val maxFace
        get() = cards.map { it.face.rankAceHigh }.maxOrNull() ?: throw IllegalStateException("maxFace must be > 0")

    protected val numberOfFaces
        get() = cards.map { it.face }
            .distinct()
            .count()

    private val numberOfSuits
        get() = cards.map { it.suit }
            .distinct()
            .count()

    protected val sortedCards = MutableCards(cards.toMutableList())

    init {
        sortedCards.sortByValues()
    }

    private fun isFlush() = numberOfSuits == 1
    private fun isStraight() = maxFace - minFace == cards.size - 1

    open fun pokerCombination(): PokerCombination {
        return when (numberOfFaces) {
            2 -> if (sortedCards.mutableCards[2].face == sortedCards.mutableCards[3].face) QUADS else FULL_HOUSE
            3 -> if (sortedCards.mutableCards[1].face == sortedCards.mutableCards[2].face) TRIPS else TWO_PAIRS
            4 -> PAIR
            5 -> {
                if (isFlush()) {
                    if (isStraight()) {
                        if (sortedCards.mutableCards[0].face == ACE) ROYAL_FLUSH else STRAIGHT_FLUSH
                    } else {
                        FLUSH
                    }
                } else {
                    if (isStraight()) STRAIGHT else HIGH_CARD
                }
            }
            else -> throw IllegalStateException("Number of faces (must be 2, 3, 4 or 5): $numberOfFaces")
        }
    }
}

class BottomRowCards(private val cards: List<Card>) : RowCards(cards) {
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

class MiddleRowCards(private val cards: List<Card>) : RowCards(cards) {
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

class TopRowCards(private val cards: List<Card>) : RowCards(cards) {
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
        val firstCardRank = sortedCards.mutableCards[0].face.rankAceHigh

        return when (pokerCombination()) {
            HIGH_CARD -> 0
            PAIR -> if (firstCardRank >= 6) firstCardRank - 5 else 0
            TRIPS -> firstCardRank + 8
            else -> throw IllegalStateException("Poker combination out of range for top row")
        }
    }
}