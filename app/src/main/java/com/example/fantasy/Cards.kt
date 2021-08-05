package com.example.fantasy

import androidx.core.text.HtmlCompat
import com.example.fantasy.PokerCombination.*

class Card(val face: CardFace, val suit: CardSuit) {
    val htmlColored = "<font color=${suit.hexColor}>${face.abbr}${suit.abbr}</font>"

    override fun toString() = "${face.abbr}${suit.abbr}"
}

open class Cards(private val cards: List<Card>) {
    private val minFace: Int = cards.map { it.face.rankAceHigh }.minOrNull() ?: throw IllegalStateException("minFace must be > 0")
    private val maxFace: Int = cards.map { it.face.rankAceHigh }.maxOrNull() ?: throw IllegalStateException("maxFace must be > 0")

    private val numberOfFaces =
        cards.map { it.face }
            .distinct()
            .count()

    private val numberOfSuits =
        cards.map { it.suit }
            .distinct()
            .count()

    fun isFlush() = numberOfSuits == 1
    fun isStraight() = maxFace - minFace == cards.size - 1

    val pokerCombination: PokerCombination
        get() =
            when (cards.size) {
                3 -> pokerCombination3()
                5 -> pokerCombination5()
                else -> throw IllegalStateException("Number of cards (must be 3 or 5): ${cards.size}")
            }

    private fun pokerCombination3(): PokerCombination {
        return when (numberOfFaces) {
            1 -> TRIPS
            2 -> PAIR
            3 -> HIGH_CARD
            else -> throw IllegalStateException("Number of faces (must be 1, 2, or 3): $numberOfFaces")
        }
    }

    private fun pokerCombination5(): PokerCombination {
        return HIGH_CARD    // todo
    }

    fun display() = HtmlCompat.fromHtml(
        cards.joinToString(" ") { it.htmlColored },
        HtmlCompat.FROM_HTML_MODE_LEGACY
    )

    override fun toString(): String {
        var result = "["
        for ((index, card) in cards.withIndex()) {
            if (index > 0) result += " "
            result += card
        }
        result += "]"
        return result
    }
}

open class MutableCards(private val mutableCards: MutableList<Card>) : Cards(mutableCards) {
    private var sortSwitch = true

    fun sort() {
        if (sortSwitch) sortByColorAndRank() else sortByRankAndColor()
        sortSwitch = !sortSwitch
    }

    private fun sortByRankAndColor() {
        sortByColor()
        sortByRank()
    }

    private fun sortByColorAndRank() {
        sortByRank()
        sortByColor()
    }

    private fun sortByRank() = mutableCards.sortByDescending { it.face.rankAceHigh }

    private fun sortByColor() = mutableCards.sortBy { it.suit }
}

class Deck(private val deckCards: MutableList<Card> = mutableListOf()) : MutableCards(deckCards) {
    fun loadFull() {
        deckCards.clear()
        CardFace.values().forEach { cardFace ->
            CardSuit.values().forEach { cardSuit ->
                deckCards.add(Card(cardFace, cardSuit))
            }
        }
    }

    fun shuffle() {
        deckCards.shuffle()
    }

    fun drawCards(quantity: Int): MutableCards {
        val drawnCards: MutableList<Card> = mutableListOf()
        for (i in 1..quantity) {
            deckCards.random().let {
                drawnCards.add(it)
                deckCards.remove(it)
            }
        }
        return MutableCards(drawnCards)
    }

}