package co.aresid.book13.fragments.addbook

import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import co.aresid.book13.R
import co.aresid.book13.Util.DatePickerVariant
import co.aresid.book13.Util.TextInputLayoutErrors
import co.aresid.book13.Util.containsBook
import co.aresid.book13.Util.disableButtonAndRenderLoadingSpinner
import co.aresid.book13.Util.enableButtonAndResetCompoundDrawablesWithIntrinsicBounds
import co.aresid.book13.Util.enableButtonAndShowCheckSignFor500Millis
import co.aresid.book13.Util.isValidAndNotDefault
import co.aresid.book13.Util.renderErrorSnackbar
import co.aresid.book13.database.bookdata.BookData
import co.aresid.book13.exceptions.BookAlreadyExistsException
import co.aresid.book13.repository.Book13Repository
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jetbrains.annotations.NotNull
import timber.log.Timber

/**
 *    Created on: 8/14/20
 *    For Project: book13
 *    Author: René Spies
 *    Copyright: © 2020 ARES ID
 */

class AddBookViewModel(application: Application): AndroidViewModel(application) {

    var bookTitle = ""
    var bookAuthor = ""
    var bookPages = ""
    var bookStartDateInMilliseconds = -1L
    var bookFinishDateInMilliseconds = -1L

    private val _renderDatePickerDialog = MutableLiveData<DatePickerVariant>()
    val renderDatePickerDialog: LiveData<DatePickerVariant>
        get() = _renderDatePickerDialog

    private val _editTextErrors = MutableLiveData<TextInputLayoutErrors>()
    val editTextErrors: LiveData<TextInputLayoutErrors>
        get() = _editTextErrors

    private val _clearAllEditTextFields = MutableLiveData<Boolean>()
    val clearAllEditTextFields: LiveData<Boolean>
        get() = _clearAllEditTextFields

    init {

        Timber.d("init: called")

        loadDefaultRenderDatePickerDialogValue() // Initialize LiveData

        loadDefaultEditTextErrorValue() // Initialize LiveData

    }

    /**
     * Checks if either [bookTitle], [bookAuthor] or [bookPages] is invalid and sets
     * errors via [_editTextErrors] accordingly.
     *
     * @return True if all above Strings are valid, false otherwise.
     */
    private fun isBookDataValid(): Boolean {

        Timber.d("isBookDataValid: called")

        // Check the bookTitle and set errors accordingly
        if (bookTitle.isEmpty()) {

            _editTextErrors.value = TextInputLayoutErrors.BOOK_TITLE_MISSING

            return false

        }

        // Check the bookAuthor and set errors accordingly
        if (bookAuthor.isEmpty()) {

            _editTextErrors.value = TextInputLayoutErrors.BOOK_AUTHOR_MISSING

            return false

        }

        // Check bookPages and set errors accordingly
        if (bookPages.isEmpty()) {

            _editTextErrors.value = TextInputLayoutErrors.BOOK_PAGE_COUNT_MISSING

            return false

        }

        return true

    }

    fun addBook(@NotNull view: View) = viewModelScope.launch {

        Timber.d("addBook: called")

        loadDefaultEditTextErrorValue() // Reset all errors

        // Checks if the given book data is invalid and returns if so
        if (!isBookDataValid()) {

            return@launch

        }

        val button = view as MaterialButton // Cast the given View as a MaterialButton
        button.disableButtonAndRenderLoadingSpinner() // Disable the button and render a loading animation

        val repository =
            Book13Repository.getInstance(getApplication()) // Get a Book13Repository instance

        // Create a BookData object from the given book data LiveData via two-way data binding
        val bookData = BookData(

            title = bookTitle,
            author = bookAuthor,
            numberOfPages = bookPages.toInt(), // I can assume it to be an int because the XML field is restricted to only allow integer input
            startDate = bookStartDateInMilliseconds,
            finishDate = bookFinishDateInMilliseconds

        )

        // Try to insert the bookData into the table and if it fails, show an error Snackbar
        // and reset the drawables on the button
        try {

            withContext(coroutineContext) {

                val allBooks =
                    repository.getAllBookData() // Retrieve all books to check for a possible duplicate

                // Check if a book with matching title, author and page count already exists and exit
                if (allBooks.containsBook(bookData)) {

                    _editTextErrors.value =
                        TextInputLayoutErrors.BOOK_ALREADY_EXISTS // Render the appropriate error message

                    throw BookAlreadyExistsException() // Throw this exception to skip ahead

                }

                repository.insertBookData(bookData) // Insert bookData into table

            }

            clearAllEditTextFields()

            button.enableButtonAndShowCheckSignFor500Millis() // Re-enable the button and show a check sign

        } catch (exception: Exception) {

            Timber.e(exception)

            if (exception !is BookAlreadyExistsException) {

                button.renderErrorSnackbar(button.context.getString(R.string.standard_error_message)) // Render an error Snackbar on the screen

            }

            button.enableButtonAndResetCompoundDrawablesWithIntrinsicBounds() // Re-enable the button and reset the loading animation

        }

    }

    private fun clearAllEditTextFields() {

        Timber.d("clearAllEditTextFields: called")

        _clearAllEditTextFields.value = true // Clear all EditText fields

    }

    /**
     * Checks if the [variant] is valid and returns if not, else
     * sets the [_renderDatePickerDialog] to [variant] to render a DatePickerDialog.
     */
    fun renderDatePickerDialog(variant: DatePickerVariant) {

        Timber.d("renderDatePickerDialog: called")

        if (!variant.isValidAndNotDefault()) {

            return

        }

        _renderDatePickerDialog.value = variant

    }

    /**
     * Sets the [_editTextErrors] value to [TextInputLayoutErrors.DEFAULT].
     */
    private fun loadDefaultEditTextErrorValue() {

        Timber.d("loadDefaultEditTextErrorValue: called")

        _editTextErrors.value = TextInputLayoutErrors.DEFAULT

    }

    /**
     * Sets the [_renderDatePickerDialog] value to [DatePickerVariant.DEFAULT].
     */
    private fun loadDefaultRenderDatePickerDialogValue() {

        Timber.d("loadDefaultRenderDatePickerDialogValue: called")

        _renderDatePickerDialog.value = DatePickerVariant.DEFAULT

    }

    /**
     * Resets the [_renderDatePickerDialog] value via a call to [loadDefaultRenderDatePickerDialogValue].
     */
    fun datePickerDialogShown() {

        Timber.d("datePickerDialogShown: called")

        loadDefaultRenderDatePickerDialogValue()

    }

}