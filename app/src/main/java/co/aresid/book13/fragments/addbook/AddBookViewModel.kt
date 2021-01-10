package co.aresid.book13.fragments.addbook

import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import co.aresid.book13.R
import co.aresid.book13.Util.disableAndShowLoadingSpinner
import co.aresid.book13.Util.enableAndResetCompoundDrawablesWithIntrinsicBounds
import co.aresid.book13.Util.enableAndShowCheckFor500Millis
import co.aresid.book13.Util.isValidAndNotInit
import co.aresid.book13.Util.showErrorSnackbar
import co.aresid.book13.database.bookdata.BookData
import co.aresid.book13.repository.Book13Repository
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

/**
 *    Created on: 8/14/20
 *    For Project: book13
 *    Author: René Spies
 *    Copyright: © 2020 ARES ID
 */

class AddBookViewModel(application: Application): AndroidViewModel(application) {
	
	// TODO: 11/23/2020 Clear all fields when the books is successfully stored   
	
	var bookTitle = ""
	var bookAuthor = ""
	var bookPages = ""
	var bookStartDateInMilliseconds = -1L
	var bookFinishDateInMilliseconds = -1L
	
	private val _renderDatePickerDialog = MutableLiveData<DatePickerVariant>()
	val renderDatePickerDialog: LiveData<DatePickerVariant>
		get() = _renderDatePickerDialog
	
	private val _editTextErrors = MutableLiveData<EditTextErrors>()
	val editTextErrors: LiveData<EditTextErrors>
		get() = _editTextErrors
	
	init {
		
		Timber.d("init: called")
		
		initializeRenderDatePickerDialogValue()
		
		initializeEditTextErrorValue()
		
	}
	
	fun addBook(view: View) = viewModelScope.launch {
		
		Timber.d("addBook: called")
		
		initializeEditTextErrorValue()
		
		if (bookTitle.isEmpty()) {
			
			_editTextErrors.value = EditTextErrors.BOOK_TITLE_MISSING
			
			return@launch
			
		}
		
		if (bookAuthor.isEmpty()) {
			
			_editTextErrors.value = EditTextErrors.BOOK_AUTHOR_MISSING
			
			return@launch
			
		}
		
		if (bookPages.isEmpty()) {
			
			_editTextErrors.value = EditTextErrors.BOOK_PAGE_COUNT_MISSING
			
			return@launch
			
		}
		
		val repository = Book13Repository.getInstance(getApplication())
		
		val bookData = BookData(
			
			title = bookTitle,
			author = bookAuthor,
			numberOfPages = bookPages.toInt(),
			startDate = bookStartDateInMilliseconds,
			finishDate = bookFinishDateInMilliseconds
		
		)
		
		// Show a loading spinner on the button
		val button = view as MaterialButton
		button.disableAndShowLoadingSpinner()
		
		// Try to insert the bookData into the table and if it fails, show an error snackbar
		// and reset the drawables on the button
		try {
			
			withContext(Dispatchers.IO) {
				
				repository.insertBookData(bookData)
				
			}
			
			button.enableAndShowCheckFor500Millis()
			
		}
		catch (e: Exception) {
			
			button.enableAndResetCompoundDrawablesWithIntrinsicBounds()
			button.showErrorSnackbar(button.context.getString(R.string.standard_error_message))
			
		}
		
	}
	
	fun renderDatePickerDialog(variant: DatePickerVariant) {
		
		Timber.d("renderDatePickerDialog: called")
		
		if (!variant.isValidAndNotInit()) {
			
			return
			
		}
		
		_renderDatePickerDialog.value = variant
		
	}
	
	private fun initializeEditTextErrorValue() {
		
		Timber.d("initializeEditTextErrorValue: called")
		
		_editTextErrors.value = EditTextErrors.INIT
		
	}
	
	private fun initializeRenderDatePickerDialogValue() {
		
		Timber.d("initializeRenderDatePickerDialogValue: called")
		
		_renderDatePickerDialog.value = DatePickerVariant.INIT
		
	}
	
	fun datePickerDialogShown() {
		
		Timber.d("datePickerDialogShown: called")
		
		initializeRenderDatePickerDialogValue()
		
	}
	
}

enum class EditTextErrors {
	
	INIT,
	
	BOOK_TITLE_MISSING,
	
	BOOK_AUTHOR_MISSING,
	
	BOOK_PAGE_COUNT_MISSING
	
}

enum class DatePickerVariant {
	
	INIT,
	
	START,
	
	FINISH
	
}