package com.wordguessing.screens.game

import android.os.CountDownTimer
import android.text.format.DateUtils
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel

private val CORRECT_BUZZ_PATTERN = longArrayOf(100, 100, 100, 100, 100, 100)
private val PANIC_BUZZ_PATTERN = longArrayOf(0, 200)
private val GAME_OVER_BUZZ_PATTERN = longArrayOf(0, 2000)
private val SKIP_BUZZ_PATTERN = longArrayOf(0, 400)
private val NO_BUZZ_PATTERN = longArrayOf(0)


class GameViewModel : ViewModel() {

    enum class BuzzType(val pattern: LongArray) {
        CORRECT(CORRECT_BUZZ_PATTERN),
        GAME_OVER(GAME_OVER_BUZZ_PATTERN),
        SKIP(SKIP_BUZZ_PATTERN),
        COUNTDOWN_PANIC(PANIC_BUZZ_PATTERN),
        NO_BUZZ(NO_BUZZ_PATTERN)
    }

    companion object {
        // These represent different important times
        // This is when the game is over
        const val DONE = 0L
        // This is the time when the phone will start buzzing each second
        private const val COUNTDOWN_PANIC_SECONDS = 10L
        // This is the number of milliseconds in a second
        const val ONE_SECOND = 1000L
        // This is the total time of the game
        const val COUNTDOWN_TIME = 60000L
    }

    // Event that triggers the phone to buzz using different patterns, determined by BuzzType
    private val _eventBuzz = MutableLiveData<BuzzType>()
    val eventBuzz: LiveData<BuzzType>
        get() = _eventBuzz

    private val timer : CountDownTimer

    // Backing Property Concept
    // The current word
    private val _remainTime = MutableLiveData<Long>()
    val remainTime: LiveData<Long>
        get()= _remainTime

    val remainTimeString = Transformations.map(remainTime){
        DateUtils.formatElapsedTime(it)
    }

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
                if (millisUntilFinished / ONE_SECOND <= COUNTDOWN_PANIC_SECONDS) {
                    _eventBuzz.value = BuzzType.COUNTDOWN_PANIC
                }
            }

            override fun onFinish() {
                _remainTime.value = DONE
                _eventBuzz.value = BuzzType.GAME_OVER
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
        _eventBuzz.value = BuzzType.SKIP
        nextWord()
    }

    fun onCorrect() {
        _score.value = (_score.value)?.plus( 1)
        _eventBuzz.value = BuzzType.CORRECT
        nextWord()
    }

    fun resetGameFinished(){
        _eventGameFinished.value = false
    }

    fun onBuzzComplete() {
        _eventBuzz.value = BuzzType.NO_BUZZ
    }


}