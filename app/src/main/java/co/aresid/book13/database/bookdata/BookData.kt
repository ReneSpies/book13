package co.aresid.book13.database.bookdata

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import co.aresid.book13.database.DatabaseNames

/**
 *    Created on: 02.09.2020
 *    For Project: book13
 *    Author: René Spies
 *    Copyright: © 2020 ARES ID
 */

@Entity(
	tableName = DatabaseNames.Table.BookData.NAME
)
data class BookData(
	
	@PrimaryKey(autoGenerate = true) @ColumnInfo(name = DatabaseNames.Table.BookData.Column.ID) val id: Long = 0,
	
	@ColumnInfo(name = DatabaseNames.Table.BookData.Column.TITLE) val title: String,
	
	@ColumnInfo(name = DatabaseNames.Table.BookData.Column.AUTHOR) val author: String,
	
	@ColumnInfo(name = DatabaseNames.Table.BookData.Column.NUMBER_OF_PAGES) val numberOfPages: Int,
	
	@ColumnInfo(name = DatabaseNames.Table.BookData.Column.START_DATE) val startDate: Long,
	
	@ColumnInfo(name = DatabaseNames.Table.BookData.Column.FINISH_DATE) val finishDate: Long

) {
	
	override fun toString(): String {
		
		// Don't change the schema here. The books ID must stay at index 0!
		
		return "$id - $title by $author with $numberOfPages pages"
		
	}
	
}