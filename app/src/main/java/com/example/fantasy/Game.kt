package com.example.fantasy

object Game {
    val deck = Deck()

    fun start() {
        deck.loadFull()
        deck.cards.shuffle()
    }
}