package com.example.fantasy

class Game(val players: Players) {
    val deck = Deck()

    fun start() {
        deck.loadFull()
        deck.cards.shuffle()
    }
}