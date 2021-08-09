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

        val game = Game()
        do {
            game.start()
            game.topRowCards = TopRowCards(game.deck.drawCards(3).cards)
            game.middleRowCards = MiddleRowCards(game.deck.drawCards(5).cards)
            game.bottomRowCards = BottomRowCards(game.deck.drawCards(5).cards)
        } while (game.bottomRowCards.value() == 0)

        binding.textViewTopRow.text = game.topRowCards.sortedCards.display()
        binding.textViewMiddleRow.text = game.middleRowCards.sortedCards.display()
        binding.textViewBottomRow.text = game.bottomRowCards.sortedCards.display()
        binding.textViewRowsValues.text = getString(R.string.result_values, if (game.isValidResult()) "OK " else "X " + game.resultValue.toString(), game.topRowCards.value().toString(), game.middleRowCards.value().toString(), game.bottomRowCards.value().toString())
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