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
	
	val book = String()
	var startPageCount = -1
	var finishPageCount = -1
	
	private val _showStartDatePickerDialog = MutableLiveData<Boolean>()
	val showStartDatePickerDialog: LiveData<Boolean>
		get() = _showStartDatePickerDialog
	
	private val _showFinishDatePickerDialog = MutableLiveData<Boolean>()
	val showFinishDatePickerDialog: LiveData<Boolean>
		get() = _showFinishDatePickerDialog
	
	private val _showEditTextErrors = MutableLiveData<EditTextErrors>()
	val showEditTextErrors: LiveData<EditTextErrors>
		get() = _showEditTextErrors
	
	var trackingStartDateInMilliseconds = -1L
	var trackingFinishDateInMilliseconds = -1L
	private val allBooks = mutableListOf<String>()
	
	init {
		
		Timber.d("init: called")
		
		// Initialize the AutoCompleteTextView's adapter value
		_booksAutoCompleteTextViewAdapter.value = ArrayAdapter<String>(
			application.baseContext,
			android.R.layout.simple_spinner_dropdown_item
		)
		
		populateAutoCompleteTextViewAdapterFromDatabase()
		
		initializeShowStartDatePickerValue()
		initializeShowFinishDatePickerDialog()
		
		_showEditTextErrors.value = EditTextErrors.INIT
		
	}
	
	private fun initializeShowStartDatePickerValue() {
		
		Timber.d("initializeShowStartDatePickerValue: called")
		
		_showStartDatePickerDialog.value = false
		
	}
	
	private fun initializeShowFinishDatePickerDialog() {
		
		Timber.d("initializeShowFinishDatePickerDialog: called")
		
		_showFinishDatePickerDialog.value = false
		
	}
	
	fun showStartDatePickerDialog() {
		
		Timber.d("showStartDatePickerDialog: called")
		
		_showStartDatePickerDialog.value = true
		
	}
	
	fun showFinishDatePickerDialog() {
		
		Timber.d("showFinishDatePickerDialog: called")
		
		_showFinishDatePickerDialog.value = true
		
	}
	
	fun startDatePickerDialogShown() {
		
		Timber.d("startDatePickerDialogShown: called")
		
		initializeShowStartDatePickerValue()
		
	}
	
	fun finishDatePickerDialogShown() {
		
		Timber.d("finishDatePickerDialogShown: called")
		
		initializeShowFinishDatePickerDialog()
		
	}
	
	private fun populateAutoCompleteTextViewAdapterFromDatabase() = viewModelScope.launch {
		
		Timber.d("populateAutoCompleteTextViewAdapterFromDatabase: called")
		
		val repository = Book13Repository.getInstance(getApplication())
		
		try {
			
			withContext(Dispatchers.IO) {
				
				repository.getAllBookData().forEach { book ->
					
					allBooks.add(book.toString())
					
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
			allBooks
		)
		
	}
	
	fun addBook() {
		
		Timber.d("addBook: called")
		
		if (!allBooks.contains(book)) {
			
			_showEditTextErrors.value = EditTextErrors.NO_BOOK_FOUND
			
		}
		
		if (book.isBlank()) {
			
			_showEditTextErrors.value = EditTextErrors.BOOK_TITLE_MISSING
			
		}
		
		if (startPageCount == -1) {
			
			_showEditTextErrors.value = EditTextErrors.START_PAGE_COUNT_MISSING
			
		}
		
		if (finishPageCount == -1) {
			
			_showEditTextErrors.value = EditTextErrors.FINISH_PAGE_COUNT_MISSING
			
		}
		
	}
	
}

enum class EditTextErrors {
	
	INIT,
	
	NO_BOOK_FOUND,
	
	BOOK_TITLE_MISSING,
	
	START_PAGE_COUNT_MISSING,
	
	FINISH_PAGE_COUNT_MISSING,
	
}
