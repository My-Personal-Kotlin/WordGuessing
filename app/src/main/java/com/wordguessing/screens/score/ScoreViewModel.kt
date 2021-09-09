package com.wordguessing.screens.score

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ScoreViewModel(finalScore:Int):ViewModel() {


    private val _eventPlayAgain = MutableLiveData<Boolean>()
    val eventPlayAgain: LiveData<Boolean>
        get() = _eventPlayAgain

    // Backing Property Concept
    // The current word
    private val _finalScore= MutableLiveData<Int>()
    val finalScore: LiveData<Int>
        get()= _finalScore

    init {
        _finalScore.value = finalScore
        Log.v("ScoreViewModel","Fonal Score = $finalScore")
    }

    fun onPlayAgain() {
        _eventPlayAgain.value = true
    }

    fun onPlayAgainComplete() {
        _eventPlayAgain.value = false
    }
}