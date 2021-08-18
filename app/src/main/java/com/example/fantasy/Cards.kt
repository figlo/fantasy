package com.example.fantasy

import androidx.core.text.HtmlCompat

data class Card(val face: CardFace, val suit: CardSuit) {
    val htmlColored = "<font color=${suit.hexColor}>${face.abbr}${suit.abbr}</font>"

    override fun toString() = "${face.abbr}${suit.abbr}"
}

open class Cards(val cards: MutableList<Card>) {
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

    private fun sortByRank() = cards.sortByDescending { it.face.rankAceHigh }

    private fun sortByColor() = cards.sortBy { it.suit }

    fun sortByValues() {            // TODO protected?
        cards.sortWith(compareByDescending<Card> { card -> cards.count { it.face == card.face } }.thenByDescending { it.face.rankAceHigh })
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

class Deck(cards: MutableList<Card> = mutableListOf()) : Cards(cards) {
    fun loadFull() {
        cards.clear()
        CardFace.values().forEach { cardFace ->
            CardSuit.values().forEach { cardSuit ->
                cards.add(Card(cardFace, cardSuit))
            }
        }
    }

    fun drawCards(quantity: Int): Cards {
        require(quantity > 0 && quantity <= cards.size) { "quantity (must be > 0 and <= size of deck.cards): $quantity" }
        val drawnCards: MutableList<Card> = mutableListOf()
        for (i in 1..quantity) {
            cards.random().let {
                drawnCards.add(it)
                cards.remove(it)
            }
        }
        return Cards(drawnCards)
    }
}