package com.example.fantasy

import androidx.core.text.HtmlCompat

class Card(val face: CardFace, val suit: CardSuit) {
    val htmlColored = "<font color=${suit.hexColor}>${face.abbr}${suit.abbr}</font>"

    override fun toString() = "${face.abbr}${suit.abbr}"
}

open class Cards(private val cards: List<Card>) {
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

    fun sortByValues() {
        val tempList =
            mutableCards.sortedBy { -it.face.rankAceHigh }
                .groupingBy { it.face }
                .eachCount()
                .toList()
                .sortedBy { (_, value) -> -value }
        val tempCards = mutableCards.toMutableList()
        mutableCards.clear()
        var card: Card
        for ((key, value) in tempList) {
            for (i in 1..value) {
                card = tempCards.first { it.face == key }
                tempCards.remove(card)
                mutableCards.add(card)
            }
        }
    }
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