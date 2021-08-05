package com.example.fantasy

import com.example.fantasy.PokerCombination.*

interface Row {
    fun value(): Int
}

class BottomRow(val groupOfCards: MutableList<Card>) : Cards(groupOfCards), Row {
    override fun value(): Int {
        return when (pokerCombination) {
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

class MiddleRow(val groupOfCards: MutableList<Card>) : Cards(groupOfCards), Row {
    override fun value(): Int {
        return when (pokerCombination) {
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

class TopRow(private val groupOfCards: MutableList<Card>) : Cards(groupOfCards), Row {
    override fun value(): Int {
        return when (pokerCombination) {
            HIGH_CARD -> 0
            PAIR -> topRowPair()
            TRIPS -> topRowTrips()
            else -> throw IllegalStateException("Poker combination out of range for top row")
        }
    }

    private fun topRowPair(): Int {
        val histogram = groupOfCards.groupingBy { it.face }.eachCount()
//        val sortedHistogram = histogram.toSortedMap(compareBy<CardFace> {it.})
        return 9
    }

    private fun topRowTrips(): Int {
        return 22
    }
}