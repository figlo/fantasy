package com.example.fantasy

class Player(val nick: String) {
    lateinit var topRowCards: TopRowCards
    lateinit var middleRowCards: MiddleRowCards
    lateinit var bottomRowCards: BottomRowCards

    val resultValue by lazy { topRowCards.value() + middleRowCards.value() + bottomRowCards.value() }

    val topRowText by lazy { topRowCards.sortedCards.display() }
    val middleRowText by lazy { middleRowCards.sortedCards.display() }
    val bottomRowText by lazy { bottomRowCards.sortedCards.display() }
    var rowsValuesText = ""

    fun isValidResult(): Boolean {
        return when {
            middleRowCards.pokerCombination() > bottomRowCards.pokerCombination() -> false
            topRowCards.pokerCombination() > middleRowCards.pokerCombination() -> false
            middleRowCards.pokerCombination() == bottomRowCards.pokerCombination() && middleRowCards isHigherThan bottomRowCards -> false
            topRowCards.pokerCombination() == middleRowCards.pokerCombination() && topRowCards isHigherThan middleRowCards -> false
            else -> true
        }
    }
}

class Players(val players: MutableList<Player>)