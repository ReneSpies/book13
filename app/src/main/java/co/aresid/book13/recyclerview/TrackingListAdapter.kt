package co.aresid.book13.recyclerview

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import co.aresid.book13.database.trackingdata.TrackingData
import timber.log.Timber
import java.text.DateFormat

/**
 *    Created on: 8/14/20
 *    For Project: book13
 *    Author: René Spies
 *    Copyright: © 2020 ARES ID
 */

class TrackingListAdapter(private val trackingList: List<TrackingData>):
    RecyclerView.Adapter<TrackingListViewHolder>() {

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

        val shortDateFormatter = DateFormat.getDateInstance(DateFormat.SHORT)
        val trackingData = trackingList[position]

        holder.binding.apply {

            this.trackingData = trackingData

            startDate = shortDateFormatter.format(trackingData.startDate)
            finishDate = shortDateFormatter.format(trackingData.finishDate)

        }

    }

    override fun getItemCount(): Int = trackingList.size

}