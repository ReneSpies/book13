package co.aresid.book13.fragments.trackinglist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import co.aresid.book13.databinding.FragmentTrackingListBinding
import co.aresid.book13.recyclerview.TrackingListAdapter
import timber.log.Timber

/**
 *    Created on: 8/14/20
 *    For Project: book13
 *    Author: René Spies
 *    Copyright: © 2020 ARES ID
 */

class TrackingListFragment: Fragment() {
	
	// Binding for the layout
	private lateinit var binding: FragmentTrackingListBinding
	
	// Corresponding ViewModel
	private lateinit var trackingListViewModel: TrackingListViewModel
	
	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		
		Timber.d("onCreateView: called")
		
		super.onCreateView(
			inflater,
			container,
			savedInstanceState
		)
		
		// Define the binding and inflate the layout
		binding = FragmentTrackingListBinding.inflate(
			inflater,
			container,
			false
		)
		
		binding.lifecycleOwner = viewLifecycleOwner
		
		// Define the ViewModel
		trackingListViewModel = ViewModelProvider(this).get(TrackingListViewModel::class.java)
		
		binding.viewModel = trackingListViewModel
		
		trackingListViewModel.trackingListAdapter.observe(viewLifecycleOwner,
		                                                  {
			
			                                                  binding.trackingListRecyclerView.adapter = it
			
		                                                  })
		
		// Construct the RecyclerView
		val recyclerViewLayoutManager = LinearLayoutManager(context)
		val trackingListAdapter = TrackingListAdapter(listOf())
		
		binding.trackingListRecyclerView.apply {
			
			// The views size does not change
			setHasFixedSize(true)
			
			layoutManager = recyclerViewLayoutManager
			
			adapter = trackingListAdapter
			
		}
		
		// Return the inflated layout
		return binding.root
		
	}
	
}