package com.example.fantasy

import androidx.core.text.HtmlCompat

class Card(val cardFace: CardFace, val cardSuit: CardSuit) {
    val htmlColored = "<font color=${cardSuit.suitHexColor}>${cardFace.abbr}${cardSuit.abbr}</font> "

    override fun toString() = "${cardFace.abbr}${cardSuit.abbr}"
}

open class GroupOfCards(private val groupOfCards: List<Card>) {
    val numberOfFaces: Int
        get() {
            return groupOfCards.map {it.cardFace}
                .distinct()
                .count()
        }

    val numberOfSuits: Int
        get() {
            return groupOfCards.map {it.cardSuit}
                .distinct()
                .count()
        }

    fun displayCards() = HtmlCompat.fromHtml(
        groupOfCards.joinToString("") { it.htmlColored },
        HtmlCompat.FROM_HTML_MODE_LEGACY
    )

    override fun toString(): String {
        var result = "["
        for ((index, card) in groupOfCards.withIndex()) {
            if (index > 0) result += " "
            result += card
        }
        result += "]"
        return result
    }
}

open class MutableGroupOfCards(private val groupOfCards: MutableList<Card>) : GroupOfCards(groupOfCards) {
    fun takeRandomCards(quantity: Int): MutableGroupOfCards {
        val cards: MutableList<Card> = mutableListOf()
        for (i in 1..quantity) {
            groupOfCards.random().let {
                cards.add(it)
                groupOfCards.remove(it)
            }
        }
        return MutableGroupOfCards(cards)
    }

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

    private fun sortByRank() = groupOfCards.sortByDescending { it.cardFace.rankAceHigh }

    private fun sortByColor() = groupOfCards.sortBy { it.cardSuit }
}

class Deck(private val groupOfCards: MutableList<Card> = mutableListOf()) : MutableGroupOfCards(groupOfCards) {
    fun loadFullDeck() {
        groupOfCards.clear()
        CardFace.values().forEach { cardFace ->
            CardSuit.values().forEach { cardSuit ->
                groupOfCards.add(Card(cardFace, cardSuit))
            }
        }
    }
}