package co.aresid.book13.fragments.starttracking

import android.app.Application
import android.widget.ArrayAdapter
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import co.aresid.book13.Util.DatePickerVariant
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
	
	var book = ""
	var startPageCount = ""
	var finishPageCount = ""
	
	private val _renderDatePickerDialog = MutableLiveData<DatePickerVariant>()
	val renderDatePickerDialog: LiveData<DatePickerVariant>
		get() = _renderDatePickerDialog
	
	private val _renderEditTextErrors = MutableLiveData<EditTextErrors>()
	val renderEditTextErrors: LiveData<EditTextErrors>
		get() = _renderEditTextErrors
	
	var trackingStartDateInMilliseconds = -1L
	var trackingFinishDateInMilliseconds = -1L
	private val allBooks = mutableListOf<String>()
	
	init {
		
		Timber.d("init: called")
		
		populateAutoCompleteTextViewAdapterFromDatabase() // Retrieve all book data from the database and load it into the adapter
		
		// Initialize all LiveData
		loadDefaultAutoCompleteTextViewValue()
		loadDefaultRenderDatePickerValue()
		loadDefaultRenderEditTextErrorsValue()
		
	}
	
	/**
	 * Loads the default value for [_booksAutoCompleteTextViewAdapter].
	 * The default value is an [ArrayAdapter].
	 */
	private fun loadDefaultAutoCompleteTextViewValue() {
		
		Timber.d("loadDefaultAutoCompleteTextViewValue: called")
		
		// Initialize the AutoCompleteTextView's adapter value
		_booksAutoCompleteTextViewAdapter.value = ArrayAdapter<String>(
			getApplication<Application>().baseContext,
			android.R.layout.simple_spinner_dropdown_item
		)
		
	}
	
	/**
	 * Loads the default value for [_renderEditTextErrors].
	 * The default value is [EditTextErrors.DEFAULT].
	 */
	private fun loadDefaultRenderEditTextErrorsValue() {
		
		Timber.d("loadDefaultRenderEditTextErrorsValue: called")
		
		_renderEditTextErrors.value = EditTextErrors.DEFAULT
		
	}
	
	/**
	 * Loads the default value for [_renderDatePickerDialog].
	 * The default value is [DatePickerVariant.DEFAULT].
	 */
	private fun loadDefaultRenderDatePickerValue() {
		
		Timber.d("loadDefaultRenderDatePickerValue: called")
		
		_renderDatePickerDialog.value = DatePickerVariant.DEFAULT
		
	}
	
	/**
	 * Changes the [_renderDatePickerDialog] value according to
	 * the given in [variant].
	 */
	fun renderDatePickerDialog(variant: DatePickerVariant) {
		
		Timber.d("renderDatePickerDialog: called")
		
		_renderDatePickerDialog.value = variant
		
	}
	
	/**
	 * Resets the [_renderDatePickerDialog] value via calling
	 * [loadDefaultRenderDatePickerValue] to ensure it wont be
	 * re-rendered after the dialog has been acknowledged.
	 */
	fun datePickerDialogShown() {
		
		Timber.d("datePickerDialogShown: called")
		
		loadDefaultRenderDatePickerValue()
		
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
			
			_renderEditTextErrors.value = EditTextErrors.NO_BOOK_FOUND
			
		}
		
		if (book.isBlank()) {
			
			_renderEditTextErrors.value = EditTextErrors.BOOK_TITLE_MISSING
			
		}
		
		if (startPageCount == "") {
			
			_renderEditTextErrors.value = EditTextErrors.START_PAGE_COUNT_MISSING
			
		}
		
		if (finishPageCount == "") {
			
			_renderEditTextErrors.value = EditTextErrors.FINISH_PAGE_COUNT_MISSING
			
		}
		
	}
	
}

enum class EditTextErrors {
	
	DEFAULT,
	
	NO_BOOK_FOUND,
	
	BOOK_TITLE_MISSING,
	
	START_PAGE_COUNT_MISSING,
	
	FINISH_PAGE_COUNT_MISSING,
	
}
