package co.aresid.book13.fragments.trackinglist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import co.aresid.book13.recyclerview.TrackingListAdapter
import co.aresid.book13.repository.Book13Repository
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 *    Created on: 8/14/20
 *    For Project: book13
 *    Author: René Spies
 *    Copyright: © 2020 ARES ID
 */

class TrackingListViewModel(application: Application): AndroidViewModel(application) {
	
	private val _trackingListAdapter = MutableLiveData<TrackingListAdapter>()
	val trackingListAdapter: LiveData<TrackingListAdapter>
		get() = _trackingListAdapter
	
	private val _hideLoadingAndShowContent = MutableLiveData<Boolean>()
	val hideLoadingAndShowContent: LiveData<Boolean>
		get() = _hideLoadingAndShowContent
	
	init {
		
		Timber.d("init: called")
		
		loadDefaultHideLoadingAndShowContentValue()
		
		loadTrackingListAdapter()
		
	}
	
	private fun loadDefaultHideLoadingAndShowContentValue() {
		
		Timber.d("loadDefaultHideLoadingAndShowContentValue: called")
		
		_hideLoadingAndShowContent.value = false
		
	}
	
	private fun loadTrackingListAdapter() = viewModelScope.launch {
		
		Timber.d("loadTrackingListAdapter: called")
		
		val repository = Book13Repository.getInstance(getApplication())
		
		try {
			
			val allTrackingData = repository.getAllTrackingData()
			
			_trackingListAdapter.value = TrackingListAdapter(allTrackingData.reversed())
			
			_hideLoadingAndShowContent.value = true
			
		}
		catch (exception: Exception) {
			
			Timber.e(exception)
			
		}
		
	}
	
}