package co.aresid.book13.fragments.trackinglist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import co.aresid.book13.database.trackingdata.TrackingData
import co.aresid.book13.repository.Book13Repository

/**
 *    Created on: 8/14/20
 *    For Project: book13
 *    Author: René Spies
 *    Copyright: © 2020 ARES ID
 */

class TrackingListViewModel(application: Application): AndroidViewModel(application) {
	
	val allTrackingData: LiveData<List<TrackingData>>
		get() {
			
			val repository = Book13Repository.getInstance(getApplication())
			
			return repository.getAllTrackingDataLiveData()
			
		}
	
}