package com.example.fantasy

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import com.example.fantasy.databinding.ActivityGameBinding

class GameActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGameBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = binding.includedLayout.toolbar
        setSupportActionBar(toolbar)

        val preferences = PreferenceManager.getDefaultSharedPreferences(this)
        val numberOfCardsInFantasyLand = preferences.getString("number_of_cards_in_fantasy_land", "14")?.toInt()!!

        val game = Game()
        game.start()

        val playerCards = game.deck.drawCards(numberOfCardsInFantasyLand)
//        Log.d("hodnotyMin", playerCards.min.toString())
//        Log.d("hodnotyMax", playerCards.max.toString())
//        Log.d("farbyPocet", playerCards.numberOfSuits.toString())
        binding.textViewFantasyCards.text = playerCards.display()
        binding.buttonSort.visibility = View.VISIBLE

        binding.buttonSort.setOnClickListener {
            playerCards.sort()
//            playerCards.sortByValues()
            binding.textViewFantasyCards.text = playerCards.display()
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