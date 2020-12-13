package co.aresid.book13.recyclerview

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import timber.log.Timber

/**
 *    Created on: 8/14/20
 *    For Project: book13
 *    Author: René Spies
 *    Copyright: © 2020 ARES ID
 */

class TrackingListAdapter(val clickListener: TrackingListClickListener): RecyclerView.Adapter<TrackingListViewHolder>() {
	
	override fun onCreateViewHolder(
		parent: ViewGroup,
		viewType: Int
	): TrackingListViewHolder {
		
		Timber.d("onCreateViewHolder: called")
		
		return TrackingListViewHolder.from(parent)
		
	}
	
	override fun onBindViewHolder(
		holder: TrackingListViewHolder,
		position: Int
	) {
		
		Timber.d("onBindViewHolder: called")
		
		holder.binding.titleTextView.text = position.toString()
		
		holder.bind(
			clickListener,
			position
		)
		
	}
	
	override fun getItemCount(): Int = 100
	
}