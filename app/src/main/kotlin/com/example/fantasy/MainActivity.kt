package com.example.fantasy

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import com.example.fantasy.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.includedLayout.toolbar)

        val preferences = PreferenceManager.getDefaultSharedPreferences(this)
        val nickName = preferences.getString("nickName", "")

        binding.buttonDrag.setOnClickListener {
            startActivity(Intent(this, DragActivity::class.java))
        }

        binding.buttonNewGame.setOnClickListener {
            startActivity(Intent(this, GameActivity::class.java))
        }

        binding.buttonResult.setOnClickListener {
            Intent(this, ResultActivity::class.java).let { intent ->
                intent.putExtra("nick", nickName)
                startActivity(intent)
            }
        }
    }

    override fun onResume() {
        super.onResume()

        val preferences = PreferenceManager.getDefaultSharedPreferences(this)

        binding.textViewNickName.text = preferences.getString("nickName", "")

        val spinnerValue = preferences.getString("number_of_cards_in_fantasy_land", "14") ?: "14"
        val spinnerIndex = spinnerValue.toInt() - 13
        binding.spinnerNumberOfFantasyLandCards.setSelection(spinnerIndex)

        binding.spinnerNumberOfFantasyLandCards.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedItem = parent?.getItemAtPosition(position).toString()
                preferences.edit().putString("number_of_cards_in_fantasy_land", selectedItem).apply()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
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
                this.startActivity(Intent(this, SettingsActivity::class.java))
                true
            }
            R.id.about -> {
                Snackbar.make(binding.root, "About", Snackbar.LENGTH_SHORT).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}

