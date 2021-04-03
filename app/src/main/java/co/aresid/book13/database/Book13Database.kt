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
	entities = [TrackingData::class, BookData::class], // List all tables here
	version = 2,
	exportSchema = true // Export the database schema to an external file (NOT IN VCS) for future references or rollbacks
)
abstract class Book13Database: RoomDatabase() {
	
	/**
	 * @return [BookDataDao].
	 */
	abstract fun getBookDataDao(): BookDataDao
	
	/**
	 * @return [TrackingData].
	 */
	abstract fun getTrackingDataDao(): TrackingDataDao
	
	// Singleton
	companion object {
		
		// Singleton prevents multiple instances of database opening at the
		// same time.
		@Volatile
		private var INSTANCE: Book13Database? = null
		
		/**
		 * Creates a new [Book13Database] if none exists.
		 * Else, returns the existing instance.
		 *
		 * @return [Book13Database].
		 */
		fun getDatabase(context: Context): Book13Database {
			
			val temporaryInstance = INSTANCE // Create a immutable val from var INSTANCE
			
			// Return the instance if it is not null
			if (temporaryInstance != null) {
				
				return temporaryInstance
				
			}
			
			// Synchronize this over all threads to prevent ghost database instances and bad errors
			synchronized(this) {
				
				// Build a new database instance
				val instance = Room.databaseBuilder(
					
					context.applicationContext,
					Book13Database::class.java,
					DatabaseNames.Database.NAME
				
				).addMigrations(MIGRATION_1_2).build()
				
				INSTANCE = instance // Reset the INSTANCE to prevent creating multiple instances
				
				return instance
				
			}
			
		}
		
	}
	
}

// Migrate to a new version of the database
val MIGRATION_1_2: Migration = object: Migration(
	1,
	2
) {
	
	override fun migrate(database: SupportSQLiteDatabase) {
		
		database.execSQL("CREATE TABLE IF NOT EXISTS `tracking_data` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `book_title` TEXT NOT NULL, `start_page_count` INTEGER NOT NULL, `finish_page_count` INTEGER NOT NULL, `start_date` INTEGER NOT NULL, `finish_date` INTEGER NOT NULL, FOREIGN KEY(`book_title`) REFERENCES `book_data`(`title`) ON UPDATE NO ACTION ON DELETE CASCADE )")
		database.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_book_data_title` ON `book_data` (`title`)")
		
	}
	
}
