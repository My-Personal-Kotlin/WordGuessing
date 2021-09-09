package com.wordguessing.screens.game

import android.os.Bundle
import android.text.format.DateUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

        Log.v("GameViewModel","ViewModelProvider called this ! ")
        gameViewModel = ViewModelProvider(this).get(GameViewModel::class.java)

        // Inflate view and obtain an instance of the binding class
        binding = DataBindingUtil.inflate(inflater, R.layout.game_fragment, container, false)

        binding.gameViewModel = gameViewModel

        binding.setLifecycleOwner(this)

        gameViewModel.eventGameFinished.observe(viewLifecycleOwner, Observer {
            if(it){
                findNavController().navigate(GameFragmentDirections.actionGameToScore(gameViewModel.score.value?:0))
                gameViewModel.resetGameFinished()
            }
        })

        return binding.root

    }

}