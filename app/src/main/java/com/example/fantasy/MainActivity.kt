package com.example.fantasy

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.preference.PreferenceManager
import com.example.fantasy.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = binding.includedLayout.toolbar
        setSupportActionBar(toolbar)

        val preferences = PreferenceManager.getDefaultSharedPreferences(this)
        val numberOfCardsInFantasy =
            preferences.getString("number_of_cards_in_fantasy", "14")?.toByte()!!

        val game = Game()
        var playerCards = GroupOfCards(mutableListOf())

        buttonSort.visibility = View.INVISIBLE

        buttonNewGame.setOnClickListener {
            game.start()
            playerCards = game.deck.takeRandomCards(numberOfCardsInFantasy)
            textViewFantasyCards.text = playerCards.displayCards()
            buttonSort.visibility = View.VISIBLE
        }

        buttonSort.setOnClickListener {
            playerCards.sortCards()
            textViewFantasyCards.text = playerCards.displayCards()
        }
    }

    override fun onResume() {
        super.onResume()

        val preferences = PreferenceManager.getDefaultSharedPreferences(this)
        textViewNickName.text = preferences.getString("nickName", "")
        preferences.registerOnSharedPreferenceChangeListener { _, key ->
            if (key == "nickName") {
                textViewNickName.text = preferences.getString(
                    "nickName",
                    ""
                )
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.right_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.settings -> {
                val intent = Intent(this, SettingsActivity::class.java)
                this.startActivity(intent)
                true
            }
            R.id.about -> {
                Toast.makeText(this, "About", Toast.LENGTH_SHORT).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}

enum class CardFace(val abbr: Char) {
    ACE('A'),
    TWO('2'),
    THREE('3'),
    FOUR('4'),
    FIVE('5'),
    SIX('6'),
    SEVEN('7'),
    EIGHT('8'),
    NINE('9'),
    TEN('T'),
    JACK('J'),
    QUEEN('Q'),
    KING('K');

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

enum class CardSuit(val abbr: Char) {
    SPADES('s'),
    HEARTS('h'),
    DIAMONDS('d'),
    CLUBS('c');

    fun suitHexColor(): String {
        return when (this) {
            SPADES -> "#00000"           // black
            HEARTS -> "#ff0000"          // red
            DIAMONDS -> "#0000ff"        // blue
            CLUBS -> "#00ff00"           // green
        }
    }
}

class Game {
    val deck = Deck()
    fun start() {
        deck.loadFullDeck()
    }
}

class Card(val cardFace: CardFace, val cardSuit: CardSuit) {
    fun htmlColored(): String =
        "<font color=${cardSuit.suitHexColor()}>${cardFace.abbr}${cardSuit.abbr}</font> "
}

open class GroupOfCards(private val groupOfCards: MutableList<Card>) {
    fun takeRandomCards(quantity: Byte): GroupOfCards {
        val cards: MutableList<Card> = mutableListOf()
        for (i in 1..quantity) {
            groupOfCards.random().let {
                cards.add(it)
                groupOfCards.remove(it)
            }
        }
        return GroupOfCards(cards)
    }

    fun displayCards() = HtmlCompat.fromHtml(
        groupOfCards.joinToString("") { it.htmlColored() },
        HtmlCompat.FROM_HTML_MODE_LEGACY
    )

    private var sortCardsSwitch = true
    fun sortCards() {
        if (sortCardsSwitch) sortCardsByColorAndRank() else sortCardsByRankAndColor()
        sortCardsSwitch = !sortCardsSwitch
    }

    private fun sortCardsByRankAndColor() {
        sortCardsByColor()
        sortCardsByRank()
    }

    private fun sortCardsByColorAndRank() {
        sortCardsByRank()
        sortCardsByColor()
    }

    private fun sortCardsByRank() {
        val comparator = Comparator { card1: Card, card2: Card ->
            return@Comparator card2.cardFace.rank() - card1.cardFace.rank()
        }
        groupOfCards.sortWith(comparator)
    }

    private fun sortCardsByColor() = groupOfCards.sortByDescending { it.cardSuit.abbr }
}

class Deck(private val groupOfCards: MutableList<Card> = mutableListOf()) :
    GroupOfCards(groupOfCards) {
    fun loadFullDeck() {
        groupOfCards.clear()
        CardFace.values().forEach { cardFace ->
            CardSuit.values().forEach { cardSuit ->
                groupOfCards.add(
                    Card(
                        cardFace,
                        cardSuit
                    )
                )
            }
        }
    }
}

// TODO test
