package co.aresid.book13.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import co.aresid.book13.database.bookdata.BookData
import co.aresid.book13.database.bookdata.BookDataDao
import co.aresid.book13.database.trackingdata.TrackingData
import co.aresid.book13.database.trackingdata.TrackingDataDao

/**
 *    Created on: 02.09.2020
 *    For Project: book13
 *    Author: René Spies
 *    Copyright: © 2020 ARES ID
 */

@Database(
    entities = [TrackingData::class, BookData::class],
    version = 1,
    exportSchema = true
)
abstract class Book13Database : RoomDatabase() {

    /**
     * Returns [BookDataDao].
     */
    abstract fun getBookDataDao(): BookDataDao

    /**
     * Returns [TrackingData].
     */
    abstract fun getTrackingDataDao(): TrackingDataDao

    companion object {

        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: Book13Database? = null

        fun getDatabase(context: Context): Book13Database {

            val tempInstance = INSTANCE

            if (tempInstance != null) {

                return tempInstance

            }

            synchronized(this) {

                val instance = Room.databaseBuilder(

                    context.applicationContext,
                    Book13Database::class.java,
                    DatabaseNames.Database.NAME

                ).build()

                INSTANCE = instance

                return instance

            }

        }

    }

}
