package co.aresid.book13.database.bookdata

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

/**
 *    Created on: 02.09.2020
 *    For Project: book13
 *    Author: René Spies
 *    Copyright: © 2020 ARES ID
 */

@Dao
interface BookDataDao {

    @Query("SELECT * FROM book_data WHERE id = :bookId")
    suspend fun get(bookId: Long): BookData?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(data: BookData)

}