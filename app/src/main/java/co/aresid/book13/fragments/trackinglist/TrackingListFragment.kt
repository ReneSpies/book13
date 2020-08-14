package co.aresid.book13.fragments.trackinglist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import co.aresid.book13.R
import co.aresid.book13.databinding.FragmentTrackingListBinding
import co.aresid.book13.recyclerview.TrackingListAdapter
import co.aresid.book13.recyclerview.TrackingListClickListener
import timber.log.Timber


/**
 *    Created on: 8/14/20
 *    For Project: book13
 *    Author: René Spies
 *    Copyright: © 2020 ARES ID
 */


class TrackingListFragment : Fragment() {

    // Binding for the layout
    private lateinit var binding: FragmentTrackingListBinding

    // Corresponding ViewModel
    private lateinit var trackingListViewModel: TrackingListViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        Timber.d("onCreateView: called")

        super.onCreateView(inflater, container, savedInstanceState)

        // Define the binding and inflate the layout
        binding = FragmentTrackingListBinding.inflate(inflater, container, false)

        // Define the ViewModel
        trackingListViewModel = ViewModelProvider(this).get(TrackingListViewModel::class.java)

        // Initialize the RecyclerView
        val recyclerViewLayoutManager = LinearLayoutManager(context)
        val trackingListAdapter = TrackingListAdapter(TrackingListClickListener { position ->

            Timber.d("item number $position clicked")

            val arguments = Bundle()
            arguments.putInt("position", position)

            findNavController().navigate(R.id.to_trackingDetailsFragment)

        })

        binding.trackingListRecyclerView.apply {

            // I know that the items do not change in size
            // so I can set this for improved performance
            setHasFixedSize(true)

            layoutManager = recyclerViewLayoutManager

            adapter = trackingListAdapter

        }

        // Return the inflated layout
        return binding.root

    }

}