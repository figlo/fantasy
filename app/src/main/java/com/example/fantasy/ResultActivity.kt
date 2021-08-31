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
        val players = Players(listOf(player1, player2))

        Game.start()

        var resultType: String
        players.players.forEach { player ->
            player.result.apply {
                topRowCards = TopRowCards(Game.deck.drawCards(3).cards)
                middleRowCards = MiddleRowCards(Game.deck.drawCards(5).cards)
                bottomRowCards = BottomRowCards(Game.deck.drawCards(5).cards)

                resultType = when {
                    isGameFantasy() -> "F"
                    isRepeatedFantasy() -> "FF"
                    isValidResult() -> "OK"
                    else -> "X"
                }

                rowsValuesText = getString(
                    R.string.result_values,
                    nick,
                    resultType + " " + resultValue.toString(),
                    topRowCards.value().toString(),
                    middleRowCards.value().toString(),
                    bottomRowCards.value().toString()
                )
            }
        }

        binding.apply {
            textViewTopRow.text = player1.result.topRowCards.sortedCards.display()
            textViewMiddleRow.text = player1.result.middleRowCards.sortedCards.display()
            textViewBottomRow.text = player1.result.bottomRowCards.sortedCards.display()
            textViewRowsValues.text = player1.result.rowsValuesText

            textViewTopRow2.text = player2.result.topRowCards.sortedCards.display()
            textViewMiddleRow2.text = player2.result.middleRowCards.sortedCards.display()
            textViewBottomRow2.text = player2.result.bottomRowCards.sortedCards.display()
            textViewRowsValues2.text = player2.result.rowsValuesText
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.right_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {               // TODO get rid of the same code (MainActivity)
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