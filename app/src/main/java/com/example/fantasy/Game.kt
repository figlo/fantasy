package com.example.fantasy

class Game {
    val deck = Deck()
    fun start() {
        deck.loadFullDeck()
    }
}