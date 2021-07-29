package com.example.fantasy

import com.example.fantasy.PokerCombination.*

abstract class Row(groupOfCards: MutableList<Card>) : GroupOfCards(groupOfCards) {
    abstract fun value(): Int

    open fun pokerCombination(): PokerCombination {                       // TODO
        return HIGH_CARD
    }
}

class BottomRow(groupOfCards: MutableList<Card>) : Row(groupOfCards) {
    override fun value(): Int {
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

class MiddleRow(groupOfCards: MutableList<Card>) : Row(groupOfCards) {
    override fun value(): Int {
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

class TopRow(groupOfCards: MutableList<Card>) : Row(groupOfCards) {
    override fun pokerCombination(): PokerCombination {
        return HIGH_CARD
    }

    override fun value(): Int {
        return when (pokerCombination()) {
            PAIR -> topRowPair()
            TRIPS -> topRowTrips()
            else -> 0
        }
    }

    private fun topRowPair(): Int {
        return 9
    }

    private fun topRowTrips(): Int {
        return 22
    }
}