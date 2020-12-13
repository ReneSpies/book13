package co.aresid.book13.fragments.starttracking

import android.app.Application
import android.widget.ArrayAdapter
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import co.aresid.book13.repository.Book13Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

/**
 *    Created on: 8/18/20
 *    For Project: book13
 *    Author: René Spies
 *    Copyright: © 2020 ARES ID
 */

class StartTrackingViewModel(application: Application): AndroidViewModel(application) {
	
	// This adapter holds the information from the database to show on the AutoCompleteTextView
	// It is a LiveData so that it can be observed and the View be updated when the data has changed
	private val _booksAutoCompleteTextViewAdapter = MutableLiveData<ArrayAdapter<String>>()
	val booksAutoCompleteTextViewAdapter: LiveData<ArrayAdapter<String>>
		get() = _booksAutoCompleteTextViewAdapter
	
	init {
		
		Timber.d("init: called")
		
		// Initialize the AutoCompleteTextView's adapter value
		_booksAutoCompleteTextViewAdapter.value = ArrayAdapter<String>(
			application.baseContext,
			android.R.layout.simple_spinner_dropdown_item
		)
		
		populateAutoCompleteTextViewAdapterFromDatabase()
		
	}
	
	private fun populateAutoCompleteTextViewAdapterFromDatabase() = viewModelScope.launch {
		
		Timber.d("populateAutoCompleteTextViewAdapterFromDatabase: called")
		
		val repository = Book13Repository.getInstance(getApplication())
		
		val bookTitles = mutableListOf<String>()
		
		try {
			
			withContext(Dispatchers.IO) {
				
				val books = repository.getAllBookData()
				
				books.forEach {
					
					bookTitles.add("${it.title} - ${it.id}")
					
				}
				
			}
			
		}
		catch (exception: Exception) {
			
			Timber.e(
				exception,
				"Cannot get all book data from database"
			)
			
		}
		
		_booksAutoCompleteTextViewAdapter.value = ArrayAdapter<String>(
			getApplication(),
			android.R.layout.simple_spinner_dropdown_item,
			bookTitles
		)
		
	}
	
}