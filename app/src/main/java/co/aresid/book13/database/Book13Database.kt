package co.aresid.book13.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
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
	version = 2,
	exportSchema = true
)
abstract class Book13Database: RoomDatabase() {
	
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
				
				).addMigrations(MIGRATION_1_2).build()
				
				INSTANCE = instance
				
				return instance
				
			}
			
		}
		
	}
	
}

val MIGRATION_1_2: Migration = object: Migration(
	1,
	2
) {
	
	override fun migrate(database: SupportSQLiteDatabase) {
		
		database.execSQL("CREATE TABLE IF NOT EXISTS `tracking_data` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `book_title` TEXT NOT NULL, `start_page_count` INTEGER NOT NULL, `finish_page_count` INTEGER NOT NULL, `start_date` INTEGER NOT NULL, `finish_date` INTEGER NOT NULL, FOREIGN KEY(`book_title`) REFERENCES `book_data`(`title`) ON UPDATE NO ACTION ON DELETE CASCADE )")
		database.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_book_data_title` ON `book_data` (`title`)")
		
	}
	
}
