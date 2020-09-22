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

class AddBookViewModel(application: Application) : AndroidViewModel(application) {

    var bookTitle = ""
    var bookAuthor = ""
    var bookPages = ""
    var bookStartDate = -1L
    var bookFinishDate = -1L

    private val _showStartDatePickerDialog = MutableLiveData<Boolean>()
    val showStartDatePickerDialog: LiveData<Boolean>
        get() = _showStartDatePickerDialog

    private val _showFinishDatePickerDialog = MutableLiveData<Boolean>()
    val showFinishDatePickerDialog: LiveData<Boolean>
        get() = _showFinishDatePickerDialog

    private val _showStartDateView = MutableLiveData<Boolean>()
    val showStartDateView: LiveData<Boolean>
        get() = _showStartDateView

    private val _showFinishDateView = MutableLiveData<Boolean>()
    val showFinishDateView: LiveData<Boolean>
        get() = _showFinishDateView

    private val _editTextErrors = MutableLiveData<EditTextErrors>()
    val editTextErrors: LiveData<EditTextErrors>
        get() = _editTextErrors

    init {

        Timber.d("init: called")

        initializeShowStartDatePickerDialogValue()

        _showStartDateView.value = false
        _showFinishDateView.value = false

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
            startDate = bookStartDate,
            finishDate = bookFinishDate

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

        } catch (e: Exception) {

            button.enableAndResetCompoundDrawablesWithIntrinsicBounds()
            button.showErrorSnackbar(button.context.getString(R.string.standard_error_message))

        }

    }

    fun showStartDatePickerDialog() {

        Timber.d("showStartDatePickerDialog: called")

        _showStartDatePickerDialog.value = true

    }

    fun showFinishDatePickerDialog() {

        Timber.d("finishDateButtonClicked: called")

        _showFinishDatePickerDialog.value = true

    }

    private fun initializeShowStartDatePickerDialogValue() {

        Timber.d("initializeShowStartDatePickerDialogValue: called")

        _showStartDatePickerDialog.value = false

    }

    private fun initializeEditTextErrorValue() {

        Timber.d("initializeEditTextErrorValue: called")

        _editTextErrors.value = EditTextErrors.INIT

    }

    private fun initializeShowFinishDatePickerDialogValue() {

        Timber.d("initializeShowFinishDatePickerDialogValue: called")

        _showFinishDatePickerDialog.value = false

    }

    fun startDatePickerDialogShown() {

        Timber.d("startDatePickerDialogShown: called")

        initializeShowStartDatePickerDialogValue()

    }

    fun finishDatePickerDialogShown() {

        Timber.d("finishDatePickerDialogShown: called")

        initializeShowFinishDatePickerDialogValue()

    }

    fun showStartDateView() {

        Timber.d("showStartDateView: called")

        _showStartDateView.value = true

    }

    fun showFinishDateView() {

        Timber.d("showFinishDateView: called")

        _showFinishDateView.value = true

    }

}

enum class EditTextErrors {

    INIT,

    BOOK_TITLE_MISSING,

    BOOK_AUTHOR_MISSING,

    BOOK_PAGE_COUNT_MISSING

}