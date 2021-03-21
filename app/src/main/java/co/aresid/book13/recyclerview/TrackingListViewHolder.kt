package co.aresid.book13.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import co.aresid.book13.databinding.ItemTrackingListBinding
import co.aresid.book13.fragments.trackinglist.TrackingListFragmentDirections
import timber.log.Timber

/**
 *    Created on: 8/14/20
 *    For Project: book13
 *    Author: René Spies
 *    Copyright: © 2020 ARES ID
 */

class TrackingListViewHolder private constructor(val binding: ItemTrackingListBinding): RecyclerView.ViewHolder(binding.root) {
	
	companion object {
		
		fun from(parent: ViewGroup): TrackingListViewHolder {
			
			Timber.d("from: called")
			
			val layoutInflater = LayoutInflater.from(parent.context)
			val binding = ItemTrackingListBinding.inflate(
				layoutInflater,
				parent,
				false
			)
			
			return TrackingListViewHolder(binding)
			
		}
		
	}
	
	fun itemClicked(view: View) {
		
		Timber.d("itemClicked: called")
		
		val action = TrackingListFragmentDirections.toEditTrackingDataFragment(binding.trackingData ?: return)
		
		view.findNavController().navigate(action)
		
	}
	
}