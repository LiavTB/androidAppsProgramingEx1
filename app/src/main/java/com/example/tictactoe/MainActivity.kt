package com.example.tictactoe

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.GridLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var gridLayout: GridLayout
    private lateinit var statusTextView: TextView
    private lateinit var playAgainButton: Button

    private var activePlayer = 0 // 0: X, 1: O
    private var gameActive = true
    private var gameState = IntArray(9) { -1 } // -1: empty, 0: X, 1: O
    private val winningPositions = arrayOf(
        intArrayOf(0, 1, 2), intArrayOf(3, 4, 5), intArrayOf(6, 7, 8), // rows
        intArrayOf(0, 3, 6), intArrayOf(1, 4, 7), intArrayOf(2, 5, 8), // columns
        intArrayOf(0, 4, 8), intArrayOf(2, 4, 6) // diagonals
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        gridLayout = findViewById(R.id.gridLayout)
        statusTextView = findViewById(R.id.statusTextView)
        playAgainButton = findViewById(R.id.playAgainButton)

        for (i in 0 until gridLayout.childCount) {
            val button = gridLayout.getChildAt(i) as Button
            button.setOnClickListener { onGridButtonClick(it) }
        }

        playAgainButton.setOnClickListener { resetGame() }
    }

    private fun onGridButtonClick(view: View) {
        if (!gameActive) return

        val button = view as Button
        val tag = button.tag.toString().toInt()

        if (gameState[tag] == -1) {
            gameState[tag] = activePlayer
            button.text = if (activePlayer == 0) "X" else "O"
            button.isEnabled = false

            if (checkWin()) {
                gameActive = false
                statusTextView.text =
                    getString(R.string.player_wins, if (activePlayer == 0) "X" else "O")
                playAgainButton.visibility = View.VISIBLE
            } else if (gameState.all { it != -1 }) {
                gameActive = false
                statusTextView.text = getString(R.string.it_s_a_draw)
                playAgainButton.visibility = View.VISIBLE
            } else {
                activePlayer = 1 - activePlayer
                statusTextView.text =
                    getString(R.string.player_s_turn, if (activePlayer == 0) "X" else "O")
            }
        }
    }

    private fun checkWin(): Boolean {
        for (position in winningPositions) {
            if (gameState[position[0]] == gameState[position[1]] &&
                gameState[position[1]] == gameState[position[2]] &&
                gameState[position[0]] != -1) {
                return true
            }
        }
        return false
    }

    private fun resetGame() {
        activePlayer = 0
        gameActive = true
        gameState.fill(-1)
        statusTextView.text = getString(R.string.player_x_turn)
        playAgainButton.visibility = View.GONE

        for (i in 0 until gridLayout.childCount) {
            val button = gridLayout.getChildAt(i) as Button
            button.text = ""
            button.isEnabled = true
        }
    }
}