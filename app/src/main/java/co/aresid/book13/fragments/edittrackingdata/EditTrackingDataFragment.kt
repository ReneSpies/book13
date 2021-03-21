package co.aresid.book13.fragments.edittrackingdata

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import co.aresid.book13.database.trackingdata.TrackingData
import co.aresid.book13.databinding.FragmentEditTrackingDataBinding
import timber.log.Timber

/**
 *    Created on: 21 Mar 2021
 *    For Project: book13
 *    Author: René Jörg Spies
 *    Copyright: © 2021 René Jörg Spies
 */

class EditTrackingDataFragment: Fragment() {
	
	private lateinit var binding: FragmentEditTrackingDataBinding
	
	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		
		Timber.d("onCreateView: called")
		
		binding = FragmentEditTrackingDataBinding.inflate(
			inflater,
			container,
			false
		)
		
		val trackingData = arguments?.getParcelable<TrackingData>("trackingData")
		
		binding.trackingData = trackingData
		
		return binding.root
		
	}
	
}