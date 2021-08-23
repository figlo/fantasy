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

    private fun sortByRankAndColor() = cards.sortWith(compareBy({ -it.face.rankAceHigh }, { it.suit }))

    private fun sortByColorAndRank() = cards.sortWith(compareBy({ it.suit }, { -it.face.rankAceHigh }))

    protected fun sortByCountAndRank() = cards.sortWith(compareBy({ card -> -cards.count { it.face == card.face } }, { -it.face.rankAceHigh }))

    fun display() = HtmlCompat.fromHtml(
        cards.joinToString(" ") { it.htmlColored },
        HtmlCompat.FROM_HTML_MODE_LEGACY
    )

    override fun toString() = cards.joinToString(" ", "[", "]")
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