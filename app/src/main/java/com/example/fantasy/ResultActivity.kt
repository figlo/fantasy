package com.example.fantasy

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.fantasy.databinding.ActivityResultBinding
import kotlinx.android.synthetic.main.activity_result.*

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = binding.includedLayout.toolbar
        setSupportActionBar(toolbar)

        val game = Game()

        var topRowCards: TopRowCards
        var middleRowCards: MiddleRowCards
        var bottomRowCards: BottomRowCards

        do {
            game.start()
            topRowCards = TopRowCards(game.deck.drawCards(3).cards)
            middleRowCards = MiddleRowCards(game.deck.drawCards(5).cards)
            bottomRowCards = BottomRowCards(game.deck.drawCards(5).cards)

            game.topRowCards = topRowCards
            game.middleRowCards = middleRowCards
            game.bottomRowCards = bottomRowCards

            game.evaluate()
        } while (false)
//    } while (bottomRowCards.value() == 0 || middleRowCards.value() == 0)

        textViewTopRow.text = topRowCards.sortedCards.display()
        textViewMiddleRow.text = middleRowCards.sortedCards.display()
        textViewBottomRow.text = bottomRowCards.sortedCards.display()
        textViewRowsValues.text = getString(R.string.result_values, if (game.isValidResult()) "OK " else "X " + game.resultValue.toString(), game.topRowValue().toString(), game.middleRowValue().toString(), game.bottomRowValue().toString())
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