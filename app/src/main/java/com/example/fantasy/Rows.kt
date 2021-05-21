package com.example.fantasy

abstract class Row(groupOfCards: MutableList<Card>) : GroupOfCards(groupOfCards) {
    abstract fun pokerCombination(): PokerCombination
    abstract fun value(): Byte
}

class BottomRow(groupOfCards: MutableList<Card>) : Row(groupOfCards) {
    override fun pokerCombination(): PokerCombination {
       return PokerCombination.HIGH_CARD
    }

    override fun value(): Byte {
        return when (pokerCombination()) {
            PokerCombination.HIGH_CARD -> 0
            PokerCombination.PAIR -> 0
            PokerCombination.TWO_PAIRS -> 0
            PokerCombination.TRIPS -> 0
            PokerCombination.STRAIGHT -> 2
            PokerCombination.FLUSH -> 4
            PokerCombination.FULL_HOUSE -> 6
            PokerCombination.QUADS -> 10
            PokerCombination.STRAIGHT_FLUSH -> 15
            PokerCombination.ROYAL_FLUSH -> 25
        }
    }
}

class MiddleRow(groupOfCards: MutableList<Card>) : Row(groupOfCards) {
    override fun pokerCombination(): PokerCombination {
        return PokerCombination.HIGH_CARD
    }

    override fun value(): Byte {
        return when (pokerCombination()) {
            PokerCombination.HIGH_CARD -> 0
            PokerCombination.PAIR -> 0
            PokerCombination.TWO_PAIRS -> 0
            PokerCombination.TRIPS -> 2
            PokerCombination.STRAIGHT -> 4
            PokerCombination.FLUSH -> 8
            PokerCombination.FULL_HOUSE -> 12
            PokerCombination.QUADS -> 20
            PokerCombination.STRAIGHT_FLUSH -> 30
            PokerCombination.ROYAL_FLUSH -> 50
        }
    }

}

class TopRow(groupOfCards: MutableList<Card>) : Row(groupOfCards) {
    override fun pokerCombination(): PokerCombination {
        return PokerCombination.HIGH_CARD
    }

    override fun value(): Byte {
        return when (pokerCombination()) {
            PokerCombination.PAIR -> topRowPair()
            PokerCombination.TRIPS -> topRowTrips()
            else -> 0
        }
    }

    private fun topRowPair(): Byte {
        return 9
    }

    private fun topRowTrips(): Byte {
        return 22
    }
}