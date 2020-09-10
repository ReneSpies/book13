package co.aresid.book13.fragments.addbook

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import co.aresid.book13.database.bookdata.BookData
import co.aresid.book13.repository.Book13Repository
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

    private val _showDatePickerDialog = MutableLiveData<Boolean>()
    val showDatePickerDialog: LiveData<Boolean>
        get() = _showDatePickerDialog

    init {

        Timber.d("init: called")

        initializeShowDatePickerDialogValue()

    }

    fun addBook() = viewModelScope.launch {

        Timber.d("addBook: called")

        if (bookTitle.isEmpty() || bookAuthor.isEmpty() || bookPages.isEmpty()) {

            return@launch

            // TODO: 10/09/2020 show feedback to the user

        }

        val repository = Book13Repository.getInstance(getApplication())

        val bookData = BookData(

            title = bookTitle,
            author = bookAuthor,
            numberOfPages = bookPages.toInt(),
            startDate = bookStartDate,
            finishDate = bookFinishDate

        )

        withContext(Dispatchers.IO) {

            repository.insertBookData(bookData)

        }

        // TODO: 10/09/2020 Show feedback to the user

    }

    fun addStartDateButtonClicked() {

        Timber.d("addStartDateButtonClicked: called")

        // TODO: 09/09/2020 Start a DatePickerDialog and show the date locally converted afterwards

        _showDatePickerDialog.value = true

    }

    fun addFinishDateButtonClicked() {

        Timber.d("addFinishDateButtonClicked: called")

        // TODO: 09/09/2020 Start a DatePickerDialog and show the date locally converted afterwards

    }

    private fun initializeShowDatePickerDialogValue() {

        Timber.d("resetShowDatePickerDialogValue: called")

        _showDatePickerDialog.value = false

    }

    fun datePickerDialogShown() {

        Timber.d("datePickerDialogShown: called")

        initializeShowDatePickerDialogValue()

    }

}