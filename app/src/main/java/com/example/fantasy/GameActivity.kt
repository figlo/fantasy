package com.example.fantasy

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.preference.PreferenceManager
import com.example.fantasy.databinding.ActivityGameBinding
import kotlinx.android.synthetic.main.activity_game.*
import kotlinx.android.synthetic.main.activity_main.*

class GameActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGameBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = binding.includedLayout.toolbar
        setSupportActionBar(toolbar)

        val preferences = PreferenceManager.getDefaultSharedPreferences(this)
        val numberOfCardsInFantasyLand = preferences.getString("number_of_cards_in_fantasy_land", "14")?.toByte()!!

        val game = Game()
        game.start()

        val playerCards = game.deck.takeRandomCards(numberOfCardsInFantasyLand)
        textViewFantasyCards.text = playerCards.displayCards()
        buttonSort.visibility = View.VISIBLE

        buttonSort.setOnClickListener {
            playerCards.sortCards()
            textViewFantasyCards.text = playerCards.displayCards()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.right_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {               // TODO get rid of redundant code (MainActivity)
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

class Game {
    val deck = Deck()
    fun start() {
        deck.loadFullDeck()
    }
}

class Card(val cardFace: CardFace, val cardSuit: CardSuit) {
    val htmlColored = "<font color=${cardSuit.suitHexColor}>${cardFace.abbr}${cardSuit.abbr}</font> "
}

open class GroupOfCards(private val groupOfCards: MutableList<Card>) {
    fun takeRandomCards(quantity: Byte): GroupOfCards {
        val cards: MutableList<Card> = mutableListOf()
        for (i in 1..quantity) {
            groupOfCards.random().let {
                cards.add(it)
//                Log.d("GroupOfCards", it.cardFace.abbr.toString() + it.cardSuit.abbr.toString())
                groupOfCards.remove(it)
            }
        }
        return GroupOfCards(cards)
    }

    fun displayCards() = HtmlCompat.fromHtml(
        groupOfCards.joinToString("") { it.htmlColored },
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
            return@Comparator card2.cardFace.rankAceHigh - card1.cardFace.rankAceHigh
        }
        groupOfCards.sortWith(comparator)
    }

    private fun sortCardsByColor() = groupOfCards.sortByDescending { it.cardSuit.abbr }
}

class Deck(private val groupOfCards: MutableList<Card> = mutableListOf()) : GroupOfCards(groupOfCards) {
    fun loadFullDeck() {
        groupOfCards.clear()
        CardFace.values().forEach { cardFace ->
            CardSuit.values().forEach { cardSuit ->
                groupOfCards.add(Card(cardFace, cardSuit))
            }
        }
    }
}