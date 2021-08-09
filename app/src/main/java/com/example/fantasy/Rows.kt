package com.example.fantasy

import com.example.fantasy.CardFace.*
import com.example.fantasy.PokerCombination.*

open class RowCards(private val rowCards: MutableList<Card>) : Cards(rowCards) {
    init {
        if (rowCards.size != 3 && rowCards.size != 5) throw IllegalStateException("Number of row cards (must be 3 or 5): ${rowCards.size}")
    }

    private val minFace
        get() = rowCards.map { it.face.rankAceHigh }.minOrNull() ?: throw IllegalStateException("minFace must be > 0")

    private val maxFace
        get() = rowCards.map { it.face.rankAceHigh }.maxOrNull() ?: throw IllegalStateException("maxFace must be > 0")

    protected val numberOfFaces
        get() = rowCards.map { it.face }
            .distinct()
            .count()

    private val numberOfSuits
        get() = rowCards.map { it.suit }
            .distinct()
            .count()

    val sortedCards = Cards(rowCards)

    init {
        sortedCards.sortByValues()
    }

    fun isHigherThen(otherRowCards: Cards): Boolean {
        var rank: Int
        var otherRank: Int
        for ((index, card) in sortedCards.cards.withIndex()) {
            rank = card.face.rankAceHigh
            otherRank = otherRowCards.cards.elementAt(index).face.rankAceHigh
            return when {
                rank > otherRank -> true
                rank < otherRank -> false
                else -> continue
            }
        }
        return false
    }

    open fun pokerCombination(): PokerCombination {
        return when (numberOfFaces) {
            2 -> if (sortedCards.cards[2].face == sortedCards.cards[3].face) QUADS else FULL_HOUSE
            3 -> if (sortedCards.cards[1].face == sortedCards.cards[2].face) TRIPS else TWO_PAIRS
            4 -> PAIR
            5 -> {
                fun isFlush() = numberOfSuits == 1
                fun isStraight() = maxFace - minFace == 4 || (sortedCards.cards[0].face == ACE && sortedCards.cards[1].face == FIVE)
                if (isFlush()) {
                    if (isStraight()) {
                        if (sortedCards.cards[0].face == ACE) ROYAL_FLUSH else STRAIGHT_FLUSH
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

class TopRowCards(topRowCards: MutableList<Card>) : RowCards(topRowCards) {
    init {
        if (topRowCards.size != 3) throw IllegalStateException("Number of top row cards (must be 3): ${topRowCards.size}")
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
        val firstCardRank = sortedCards.cards[0].face.rankAceHigh

        return when (pokerCombination()) {
            HIGH_CARD -> 0
            PAIR -> if (firstCardRank >= 6) firstCardRank - 5 else 0
            TRIPS -> firstCardRank + 8
            else -> throw IllegalStateException("Poker combination out of range for top row")
        }
    }
}

class MiddleRowCards(middleRowCards: MutableList<Card>) : RowCards(middleRowCards) {
    init {
        if (middleRowCards.size != 5) throw IllegalStateException("Number of middle row cards (must be 5): ${middleRowCards.size}")
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

class BottomRowCards(bottomRowCards: MutableList<Card>) : RowCards(bottomRowCards) {
    init {
        if (bottomRowCards.size != 5) throw IllegalStateException("Number of bottom row cards (must be 5): ${bottomRowCards.size}")
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