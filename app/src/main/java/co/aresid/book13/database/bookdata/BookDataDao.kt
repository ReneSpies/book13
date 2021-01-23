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

    /**
     * Retrieves a book from the database where [bookId] is found.
     *
     * @return [BookData] object representing a row from the table.
     */
    @Query("SELECT * FROM book_data WHERE id = :bookId")
    suspend fun getById(bookId: Long): BookData?

    /**
     * Retrieves a book from the database where [bookTitle] is found.
     * Is possible due to [bookTitle] being unique.
     *
     * @return [BookData] object representing a row from the table.
     */
    @Query("SELECT * FROM book_data WHERE title = :bookTitle")
    suspend fun getByTitle(bookTitle: String): BookData?

    /**
     * Retrieves a list of all books found in the table.
     *
     * @return [List] with type [BookData] representing all rows from the table.
     */
    @Query("SELECT * FROM book_data")
    suspend fun getAll(): List<BookData>

    /**
     * Inserts a new book from [data] into the table.
     * Replaces all old data if a book with the primary key ([BookData.id]) already exists.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(data: BookData)

}