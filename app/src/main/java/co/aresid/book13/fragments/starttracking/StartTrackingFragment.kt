package co.aresid.book13.fragments.starttracking

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import co.aresid.book13.databinding.FragmentStartTrackingBinding
import timber.log.Timber


/**
 *    Created on: 8/18/20
 *    For Project: book13
 *    Author: René Spies
 *    Copyright: © 2020 ARES ID
 */


class StartTrackingFragment : Fragment() {

    // Binding for the layout
    private lateinit var binding: FragmentStartTrackingBinding

    // Corresponding ViewModel
    private lateinit var startTrackingViewModel: StartTrackingViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        Timber.d("onCreateView: called")

        // Define the binding and inflate the layout
        binding = FragmentStartTrackingBinding.inflate(inflater, container, false)

        // Define the ViewModel
        startTrackingViewModel = ViewModelProvider(this).get(StartTrackingViewModel::class.java)

        // Return the inflated layout
        return binding.root

    }

}