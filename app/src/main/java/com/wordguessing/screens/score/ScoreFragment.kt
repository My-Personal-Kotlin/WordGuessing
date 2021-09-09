package com.wordguessing.screens.score

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.wordguessing.R
import com.wordguessing.databinding.ScoreFragmentBinding

class ScoreFragment : Fragment() {

    private lateinit var viewModelFactory: ScoreViewModelFactory

    private lateinit var scoreViewModel: ScoreViewModel

    private lateinit var binding :  ScoreFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate view and obtain an instance of the binding class.
        binding = DataBindingUtil.inflate(inflater, R.layout.score_fragment, container, false)

        // 1 way
        // Get args using by navArgs property delegate
        val scoreFragmentArgs by navArgs<ScoreFragmentArgs>()
        // 2 way
        // binding.scoreText.text = ScoreFragmentArgs.fromBundle(requireArguments()).score.toString()

        viewModelFactory = ScoreViewModelFactory(scoreFragmentArgs.score)
        scoreViewModel = ViewModelProvider(this,viewModelFactory).get(ScoreViewModel::class.java)

        binding.scoreViewModel = scoreViewModel

        binding.setLifecycleOwner(this)

        scoreViewModel.eventPlayAgain.observe(viewLifecycleOwner, Observer {
            if(it){
                findNavController().navigate(ScoreFragmentDirections.actionRestart())
                scoreViewModel.onPlayAgainComplete()
            }
        })

        return binding.root
    }
}