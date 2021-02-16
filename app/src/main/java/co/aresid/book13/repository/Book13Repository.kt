package co.aresid.book13.repository

import android.app.Application
import co.aresid.book13.database.Book13Database
import co.aresid.book13.database.bookdata.BookData
import co.aresid.book13.database.bookdata.BookDataDao
import co.aresid.book13.database.trackingdata.TrackingData
import co.aresid.book13.database.trackingdata.TrackingDataDao

/**
 *    Created on: 09/09/2020
 *    For Project: book13
 *    Author: René Spies
 *    Copyright: © 2020 ARES ID
 */

class Book13Repository private constructor(private val application: Application) {

    // Database
    private lateinit var book13Database: Book13Database

    // BookDataDao
    private val bookDataDao: BookDataDao by lazy {

        if (!::book13Database.isInitialized) {

            book13Database = Book13Database.getDatabase(application.applicationContext)

        }

        book13Database.getBookDataDao()

    }

    // TrackingDataDao
    private val trackingDataDao: TrackingDataDao by lazy {

        if (!::book13Database.isInitialized) {

            book13Database = Book13Database.getDatabase(application.applicationContext)

        }

        book13Database.getTrackingDataDao()

    }

    suspend fun getBookDataById(id: Long) = bookDataDao.getById(id)

    suspend fun getAllBookData() = bookDataDao.getAll()

    suspend fun insertBookData(data: BookData) = bookDataDao.insert(data)

    suspend fun insertTrackingData(data: TrackingData) = trackingDataDao.insert(data)

    suspend fun deleteTrackingData(data: TrackingData) = trackingDataDao.delete(data)

    fun getAllTrackingDataLiveData() = trackingDataDao.getAllLiveData()

    // Make it a singleton
    companion object {

        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: Book13Repository? = null

        fun getInstance(application: Application): Book13Repository =
            INSTANCE ?: synchronized(this) {

                INSTANCE ?: Book13Repository(application).also { INSTANCE = it }

            }
    }

}