package com.example.fantasy

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.fantasy.databinding.ActivityResultBinding

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = binding.includedLayout.toolbar
        setSupportActionBar(toolbar)

        val nick = intent.getStringExtra("nick").toString()
        val player1 = Player(nick)
        val player2 = Player("Player 2")
        val players = Players(mutableListOf(player1, player2))

        val game = Game(players)
        game.start()

        for (player in players.players) {
            player.topRowCards = TopRowCards(game.deck.drawCards(3).cards)
            player.middleRowCards = MiddleRowCards(game.deck.drawCards(5).cards)
            player.bottomRowCards = BottomRowCards(game.deck.drawCards(5).cards)

            player.rowsValuesText = getString(
                R.string.result_values,
                player.nick,
                if (player.isValidResult()) "OK " else "X " + player.resultValue.toString(),
                player.topRowCards.value().toString(),
                player.middleRowCards.value().toString(),
                player.bottomRowCards.value().toString()
            )
        }

        binding.textViewTopRow.text = player1.topRowText
        binding.textViewMiddleRow.text = player1.middleRowText
        binding.textViewBottomRow.text = player1.bottomRowText
        binding.textViewRowsValues.text = player1.rowsValuesText

        binding.textViewTopRow2.text = player2.topRowText
        binding.textViewMiddleRow2.text = player2.middleRowText
        binding.textViewBottomRow2.text = player2.bottomRowText
        binding.textViewRowsValues2.text = player2.rowsValuesText
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