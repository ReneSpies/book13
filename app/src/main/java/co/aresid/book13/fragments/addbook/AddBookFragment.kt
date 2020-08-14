package co.aresid.book13.fragments.addbook

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import co.aresid.book13.databinding.FragmentAddBookBinding
import timber.log.Timber


/**
 *    Created on: 8/14/20
 *    For Project: book13
 *    Author: René Spies
 *    Copyright: © 2020 ARES ID
 */


class AddBookFragment : Fragment() {

    // Binding for the layout
    private lateinit var binding: FragmentAddBookBinding

    // Corresponding ViewModel
    private lateinit var addBookViewModel: AddBookViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        Timber.d("onCreateView: called")

        super.onCreateView(inflater, container, savedInstanceState)

        // Define the binding and inflate the layout
        binding = FragmentAddBookBinding.inflate(inflater, container, false)

        // Define the ViewModel
        addBookViewModel = ViewModelProvider(this).get(AddBookViewModel::class.java)

        // Return the inflated layout
        return binding.root

    }

}