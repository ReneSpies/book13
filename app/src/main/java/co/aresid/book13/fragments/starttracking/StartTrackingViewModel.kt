package co.aresid.book13.fragments.starttracking

import android.app.Application
import android.view.View
import android.widget.ArrayAdapter
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import co.aresid.book13.R
import co.aresid.book13.Util.DatePickerVariant
import co.aresid.book13.Util.TextInputLayoutErrors
import co.aresid.book13.Util.disableButtonAndRenderLoadingSpinner
import co.aresid.book13.Util.enableButtonAndResetCompoundDrawablesWithIntrinsicBounds
import co.aresid.book13.Util.enableButtonAndShowCheckSignFor500Millis
import co.aresid.book13.Util.renderErrorSnackbar
import co.aresid.book13.database.bookdata.BookData
import co.aresid.book13.database.trackingdata.TrackingData
import co.aresid.book13.repository.Book13Repository
import com.google.android.material.button.MaterialButton
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
	var startDateInMilliseconds = -1L
	var finishDateInMilliseconds = -1L
	
	// Triggers Util.clearText when the value is true via data binding
	private val _clearAllEditTextFields = MutableLiveData<Boolean>()
	val clearAllEditTextFields: LiveData<Boolean>
		get() = _clearAllEditTextFields
	
	// Controls whether to render the DatePickerDialog or not and which variant of it
	private val _renderDatePickerDialog = MutableLiveData<DatePickerVariant>()
	val renderDatePickerDialog: LiveData<DatePickerVariant>
		get() = _renderDatePickerDialog
	
	// Controls whether to render EditText errors or not and which errors
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
		
		val repository = Book13Repository.getInstance(getApplication())
		val allBooks = repository.getAllBookData()
		
		// Create a new ArrayAdapter with the list of all books in the database
		_booksAutoCompleteTextViewAdapter.value = ArrayAdapter<BookData>(
			getApplication(),
			R.layout.item_drop_down_text_view,
			allBooks
		)
		
	}
	
	/**
	 * Validates all given data and if it succeeds, it will create a new
	 * TrackingData object and inserts it into the database.
	 */
	fun addTrackingData(view: View) = viewModelScope.launch {
		
		Timber.d("addTrackingData: called")
		
		loadDefaultRenderEditTextErrorsValue() // Reset all errors
		
		// Check if the EditText field is blank and render the appropriate error message
		if (book.isBlank()) {
			
			_renderEditTextErrors.value = TextInputLayoutErrors.BOOK_TITLE_MISSING
			
			return@launch
			
		}
		
		val repository = Book13Repository.getInstance(getApplication()) // Get a repository instance
		
		// The first index of book is the books ID. This extracts the ID from the String
		// Therefore the BookData's toString function must have it's ID at index 0
		val bookId = book[0].toString().toLongOrNull()
		
		// Checks whether the bookId is valid and a book exists and renders the appropriate
		// error message if not
		if (!(bookId != null && repository.getBookDataById(bookId) != null)) {
			
			_renderEditTextErrors.value = TextInputLayoutErrors.NO_BOOK_FOUND
			
			return@launch
			
		}
		
		// Checks if the startPageCount is given and renders the appropriate error message if not
		if (startPageCount == "") {
			
			_renderEditTextErrors.value = TextInputLayoutErrors.START_PAGE_COUNT_MISSING
			
			return@launch
			
		}
		
		// Checks if the finishPageCount is given and renders the appropriate error message if not
		if (finishPageCount == "") {
			
			_renderEditTextErrors.value = TextInputLayoutErrors.FINISH_PAGE_COUNT_MISSING
			
			return@launch
			
		}
		
		val bookData = repository.getBookDataById(bookId)!! // Extract the BookData matching the given ID
		
		// Create a new TrackingData object from the given book, startPageCount & finishPageCount
		val trackingData = TrackingData(
			
			bookId = bookData.id,
			bookTitle = bookData.title,
			startPageCount = startPageCount.toInt(), // I can assume it to be an int because the XML field is restricted to only allow integer input
			finishPageCount = finishPageCount.toInt(), // I can assume it to be an int because the XML field is restricted to only allow integer input
			startDate = startDateInMilliseconds,
			finishDate = finishDateInMilliseconds
		
		)
		
		val button = view as MaterialButton
		button.disableButtonAndRenderLoadingSpinner()
		
		try {
			
			withContext(coroutineContext) {
				
				repository.insertTrackingData(trackingData) // Insert the newly created TrackingData into the table
				
			}
			
			_clearAllEditTextFields.value = true // Clear all EditText fields
			
			button.enableButtonAndShowCheckSignFor500Millis()
			
		}
		catch (exception: Exception) {
			
			Timber.e(exception)
			
			button.renderErrorSnackbar(button.context.getString(R.string.standard_error_message))
			button.enableButtonAndResetCompoundDrawablesWithIntrinsicBounds()
			
		}
		
	}
	
	fun populateFromTrackingData(trackingData: TrackingData) = viewModelScope.launch {
		
		Timber.d("populateFromTrackingData: called")
		
		val repository = Book13Repository.getInstance(getApplication())
		
		withContext(coroutineContext) {
			
			val foundBook = repository.getBookDataById(trackingData.bookId)
			book = foundBook.toString()
			startPageCount = trackingData.startPageCount.toString()
			finishPageCount = trackingData.startPageCount.toString()
			startDateInMilliseconds = trackingData.startDate
			finishDateInMilliseconds = trackingData.finishDate
			
		}
		
	}
	
}
