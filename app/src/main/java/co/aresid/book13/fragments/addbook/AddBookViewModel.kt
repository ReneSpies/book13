package co.aresid.book13.fragments.addbook

import android.app.Application
import androidx.lifecycle.AndroidViewModel
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

    init {

        Timber.d("init: called")

    }

    fun addBook() = viewModelScope.launch {

        Timber.d("addBook: called")

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

    }

    fun addStartDateButtonClicked() {

        Timber.d("addStartDateButtonClicked: called")

        // TODO: 09/09/2020 Start a DatePickerDialog and show the date locally converted afterwards

    }

    fun addFinishDateButtonClicked() {

        Timber.d("addFinishDateButtonClicked: called")

        // TODO: 09/09/2020 Start a DatePickerDialog and show the date locally converted afterwards

    }

}