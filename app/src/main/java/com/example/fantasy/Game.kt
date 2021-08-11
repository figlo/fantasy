package com.example.fantasy

class Game {
    val deck = Deck()

    lateinit var topRowCards: TopRowCards
    lateinit var middleRowCards: MiddleRowCards
    lateinit var bottomRowCards: BottomRowCards

    val resultValue: Int
        get() = topRowCards.value() + middleRowCards.value() + bottomRowCards.value()

    fun isValidResult(): Boolean {
        return when {
            middleRowCards.pokerCombination() > bottomRowCards.pokerCombination() -> false
            topRowCards.pokerCombination() > middleRowCards.pokerCombination() -> false
            middleRowCards.pokerCombination() == bottomRowCards.pokerCombination() && middleRowCards isHigherThan bottomRowCards -> false
            topRowCards.pokerCombination() == middleRowCards.pokerCombination() && topRowCards isHigherThan middleRowCards -> false
            else -> true
        }
    }

    fun start() {
        deck.loadFull()
        deck.shuffle()
    }
}