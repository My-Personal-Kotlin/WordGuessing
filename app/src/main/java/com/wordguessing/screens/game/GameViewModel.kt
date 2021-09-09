package com.wordguessing.screens.game

import android.os.CountDownTimer
import android.text.format.DateUtils
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel

class GameViewModel : ViewModel() {

    companion object {
        // These represent different important times
        // This is when the game is over
        const val DONE = 0L
        // This is the number of milliseconds in a second
        const val ONE_SECOND = 1000L
        // This is the total time of the game
        const val COUNTDOWN_TIME = 11200L
    }

    private val timer : CountDownTimer

    // Backing Property Concept
    // The current word
    private val _word = MutableLiveData<String>()
    val word: LiveData<String>
        get()= _word

    // Backing Property Concept
    // The current score
    private val _score = MutableLiveData<Int>()
    val score : LiveData<Int>
        get()= _score

    // Backing Property Concept
    // To check if Game Finished
    private val _eventGameFinished = MutableLiveData<Boolean>()
    val eventGameFinished : LiveData<Boolean>
        get() = _eventGameFinished

    // Backing Property Concept
    // The current word
    private val _remainTime = MutableLiveData<Long>()
    val remainTime: LiveData<Long>
        get()= _remainTime

    val remainTimeString = Transformations.map(remainTime){
        DateUtils.formatElapsedTime(it)
    }


    // The list of words - the front of the list is the next word to guess
    private lateinit var wordList: MutableList<String>


    init {
        Log.v("GameViewModel","Created")
        _eventGameFinished.value = false
        _score.value = 0
        _word.value = ""
        resetList()
        nextWord()

        timer = object : CountDownTimer(COUNTDOWN_TIME, ONE_SECOND) {

            override fun onTick(millisUntilFinished: Long) {
                _remainTime.value = millisUntilFinished / ONE_SECOND
             // TODO implement what should happen each tick of the timer
            }

            override fun onFinish() {
                _remainTime.value = DONE
                _eventGameFinished.value = true
            }
        }

        timer.start()
    }

    override fun onCleared() {
        super.onCleared()
        Log.v("GameViewModel","Cleared")
        timer.cancel()

    }

    /**
     * Resets the list of words and randomizes the order
     */
    private fun resetList() {
        wordList = mutableListOf(
            "queen",
            "hospital",
            "basketball",
            "cat",
            "change",
            "snail",
            "soup",
            "calendar",
            "sad",
            "desk",
            "guitar",
            "home",
            "railway",
            "zebra",
            "jelly",
            "car",
            "crow",
            "trade",
            "bag",
            "roll",
            "bubble"
        )
        wordList.shuffle() // thats why we have to set it to MutableList
    }

    /**
     * Moves to the next word in the list
     */
    private fun nextWord() {
        //Select and remove a word from the list
        if (wordList.isEmpty()) {
            resetList()
         //   gameFinished()
        }
        _word.value = wordList.removeAt(0)

    }

    /** Methods for buttons presses **/

    fun onSkip() {
        _score.value = (_score.value)?.minus( 1)
        nextWord()
    }

    fun onCorrect() {
        _score.value = (_score.value)?.plus( 1)
        nextWord()
    }

    fun resetGameFinished(){
        _eventGameFinished.value = false
    }

}