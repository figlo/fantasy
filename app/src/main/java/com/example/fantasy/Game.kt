package com.example.fantasy

class Game {
    val deck = Deck()

    lateinit var topRowCards: TopRowCards
    lateinit var middleRowCards: MiddleRowCards
    lateinit var bottomRowCards: BottomRowCards

    fun topRowValue() = topRowCards.value()
    fun middleRowValue() = middleRowCards.value()
    fun bottomRowValue() = bottomRowCards.value()

    private fun topRowPokerCombination() = topRowCards.pokerCombination()
    private fun middleRowPokerCombination() = middleRowCards.pokerCombination()
    private fun bottomRowPokerCombination() = bottomRowCards.pokerCombination()

    var resultValue = 0

    fun evaluate() {
        resultValue =  when {
            !isValidResult() -> 0
            else -> topRowValue() + middleRowValue() + bottomRowValue()
        }
    }

    fun isValidResult(): Boolean {
        return when {
            middleRowPokerCombination() > bottomRowPokerCombination() -> false
            topRowPokerCombination() > middleRowPokerCombination() -> false
            middleRowPokerCombination() == bottomRowPokerCombination() && middleRowCards.isHigherThen(bottomRowCards) -> false
            topRowPokerCombination() == middleRowPokerCombination() && topRowCards.isHigherThen(middleRowCards) -> false
            else -> true
        }
    }

    fun start() {
        deck.loadFull()
        deck.shuffle()
    }
}