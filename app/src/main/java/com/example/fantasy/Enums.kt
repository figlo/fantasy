package com.example.fantasy

enum class CardFace {
    ACE,
    TWO,
    THREE,
    FOUR,
    FIVE,
    SIX,
    SEVEN,
    EIGHT,
    NINE,
    TEN,
    JACK,
    QUEEN,
    KING;

    fun abbr(): Char {
        return when (this) {
            ACE -> 'A'
            TWO -> '2'
            THREE -> '3'
            FOUR -> '4'
            FIVE -> '5'
            SIX -> '6'
            SEVEN -> '7'
            EIGHT -> '8'
            NINE -> '9'
            TEN -> 'T'
            JACK -> 'J'
            QUEEN -> 'Q'
            KING -> 'K'
        }
    }

    fun rank(): Int {
        return when (this) {
            ACE -> 14
            TWO -> 2
            THREE -> 3
            FOUR -> 4
            FIVE -> 5
            SIX -> 6
            SEVEN -> 7
            EIGHT -> 8
            NINE -> 9
            TEN -> 10
            JACK -> 11
            QUEEN -> 12
            KING -> 13
        }
    }
}

enum class CardSuit {
    SPADES,
    HEARTS,
    DIAMONDS,
    CLUBS;

    fun abbr(): Char {
        return when (this) {
            SPADES -> 's'
            HEARTS -> 'h'
            DIAMONDS -> 'd'
            CLUBS -> 'c'
        }
    }

    fun suitHexColor(): String {
        return when (this) {
            SPADES -> "#00000"           // black
            HEARTS -> "#ff0000"          // red
            DIAMONDS -> "#0000ff"        // blue
            CLUBS -> "#00ff00"           // green
        }
    }
}

enum class PokerCombination {
    HIGH_CARD,
    PAIR,
    TWO_PAIRS,
    TRIPS,
    STRAIGHT,
    FLUSH,
    FULL_HOUSE,
    QUADS,
    STRAIGHT_FLUSH,
    ROYAL_FLUSH;

    fun rank(): Byte {
        return when(this) {
            HIGH_CARD -> 1
            PAIR -> 2
            TWO_PAIRS -> 3
            TRIPS -> 4
            STRAIGHT -> 5
            FLUSH -> 6
            FULL_HOUSE -> 7
            QUADS -> 8
            STRAIGHT_FLUSH -> 9
            ROYAL_FLUSH -> 10
        }
    }
}