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

private val cardFaces = arrayOf("2", "3", "4", "5", "6", "7", "8", "9", "T", "J", "Q", "K", "A")
private val cardSuits = arrayOf("s", "h", "d", "c")
private val cardRanks = cardFaces.mapIndexed { index, s -> s to index + 2 }
    .toMap()     // ranks from "2 to 2" to "A to 14"
private val cardHexColors = mapOf(
    "s" to "#000000",    // black - spades
    "h" to "#ff0000",    // red - hearts
    "d" to "#0000ff",    // blue - diamonds
    "c" to "#00ff00"     // green - clubs
)

class Game {
    val deck = Deck()
    fun start() {
        deck.loadFullDeck()
    }
}

class Card(val face: String, val suit: String) {            // TODO face and suit check
    fun rank(): Int = cardRanks[face]!!
    fun htmlColored(): String = "<font color=${cardHexColors[suit]}>$face$suit</font> "
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
            return@Comparator card2.rank() - card1.rank()
        }
        groupOfCards.sortWith(comparator)
    }

    private fun sortCardsByColor() = groupOfCards.sortByDescending { it.suit }
}

class Deck(private val groupOfCards: MutableList<Card> = mutableListOf()) :
    GroupOfCards(groupOfCards) {
    fun loadFullDeck() {
        groupOfCards.clear()
        cardFaces.forEach { face ->
            cardSuits.forEach { suit ->
                groupOfCards.add(
                    Card(
                        face,
                        suit
                    )
                )
            }
        }
    }
}

// TODO test
