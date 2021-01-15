package co.aresid.book13.fragments.starttracking

import android.app.Application
import android.widget.ArrayAdapter
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import co.aresid.book13.Util.DatePickerVariant
import co.aresid.book13.Util.TextInputLayoutErrors
import co.aresid.book13.database.bookdata.BookData
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
	private val _booksAutoCompleteTextViewAdapter = MutableLiveData<ArrayAdapter<BookData>>()
	val booksAutoCompleteTextViewAdapter: LiveData<ArrayAdapter<BookData>>
		get() = _booksAutoCompleteTextViewAdapter
	
	var book = ""
	var startPageCount = ""
	var finishPageCount = ""
	var trackingStartDateInMilliseconds = -1L
	var trackingFinishDateInMilliseconds = -1L
	
	private val _renderDatePickerDialog = MutableLiveData<DatePickerVariant>()
	val renderDatePickerDialog: LiveData<DatePickerVariant>
		get() = _renderDatePickerDialog
	
	private val _renderEditTextErrors = MutableLiveData<TextInputLayoutErrors>()
	val renderEditTextErrors: LiveData<TextInputLayoutErrors>
		get() = _renderEditTextErrors
	
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
		_booksAutoCompleteTextViewAdapter.value = ArrayAdapter<BookData>(
			getApplication<Application>().baseContext,
			android.R.layout.simple_spinner_dropdown_item
		)
		
	}
	
	private suspend fun getAllBookData(): List<BookData> {
		
		Timber.d("getAllBookData: called")
		
		var allBooks = listOf<BookData>()
		
		withContext(viewModelScope.coroutineContext + Dispatchers.IO) {
			
			val repository = Book13Repository.getInstance(getApplication())
			
			try {
				
				// Get all books from the database and iterate over it
				allBooks = repository.getAllBookData()
				
			}
			catch (exception: Exception) {
				
				Timber.e(
					exception,
					"Cannot get all book data from database"
				)
				
			}
			
		}
		
		return allBooks
		
	}
	
	/**
	 * Loads the default value for [_renderEditTextErrors].
	 * The default value is [TextInputLayoutErrors.DEFAULT].
	 */
	private fun loadDefaultRenderEditTextErrorsValue() {
		
		Timber.d("loadDefaultRenderEditTextErrorsValue: called")
		
		_renderEditTextErrors.value = TextInputLayoutErrors.DEFAULT
		
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
	
	/**
	 * Retrieves all books from the database and inserts them into an [ArrayAdapter]
	 * and updates the [_booksAutoCompleteTextViewAdapter] value with the new ArrayAdapter.
	 */
	private fun populateAutoCompleteTextViewAdapterFromDatabase() = viewModelScope.launch {
		
		Timber.d("populateAutoCompleteTextViewAdapterFromDatabase: called")
		
		val allBooks = getAllBookData()
		
		// Create a new ArrayAdapter with the list of all books in the database
		_booksAutoCompleteTextViewAdapter.value = ArrayAdapter<BookData>(
			getApplication(),
			android.R.layout.simple_spinner_dropdown_item,
			allBooks
		)
		
	}
	
	/**
	 * Validates all given data and if it succeeds, it will create a new
	 * TrackingData object and inserts it into the database.
	 */
	fun addTrackingData() {
		
		Timber.d("addTrackingData: called")
		
		//		if (!allBooks.contains(book)) {
		//
		//			_renderEditTextErrors.value = TextInputLayoutErrors.NO_BOOK_FOUND
		//
		//		}
		
		if (book.isBlank()) {
			
			_renderEditTextErrors.value = TextInputLayoutErrors.BOOK_TITLE_MISSING
			
		}
		
		if (startPageCount == "") {
			
			_renderEditTextErrors.value = TextInputLayoutErrors.START_PAGE_COUNT_MISSING
			
		}
		
		if (finishPageCount == "") {
			
			_renderEditTextErrors.value = TextInputLayoutErrors.FINISH_PAGE_COUNT_MISSING
			
		}
		
	}
	
}
