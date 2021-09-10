package com.wordguessing.screens.game

import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.getSystemService
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.wordguessing.R
import com.wordguessing.databinding.GameFragmentBinding

class GameFragment : Fragment() {

    private lateinit var gameViewModel: GameViewModel

    private lateinit var binding: GameFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Inflate view and obtain an instance of the binding class
        binding = DataBindingUtil.inflate(inflater, R.layout.game_fragment, container, false)

        Log.v("GameViewModel","ViewModelProvider called this ! ")
        gameViewModel = ViewModelProvider(this).get(GameViewModel::class.java)

        binding.gameViewModel = gameViewModel

        binding.setLifecycleOwner(this)

        gameViewModel.eventGameFinished.observe(viewLifecycleOwner, Observer {
            if(it){
                findNavController().navigate(GameFragmentDirections.actionGameToScore(gameViewModel.score.value?:0))
                gameViewModel.resetGameFinished()
            }
        })

        gameViewModel.eventBuzz.observe(viewLifecycleOwner, Observer { buzzType ->
            if (buzzType != GameViewModel.BuzzType.NO_BUZZ) {
                buzz(buzzType.pattern)
                gameViewModel.onBuzzComplete()
            }
        })

        return binding.root

    }

    /**
     * Given a pattern, this method makes sure the device buzzes
     */
    private fun buzz(pattern: LongArray) {
        val buzzer = activity?.getSystemService<Vibrator>()
        buzzer?.let {
            // Vibrate for 500 milliseconds
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                buzzer.vibrate(VibrationEffect.createWaveform(pattern, -1))
            } else {
                //deprecated in API 26
                buzzer.vibrate(pattern, -1)
            }
        }
    }
}