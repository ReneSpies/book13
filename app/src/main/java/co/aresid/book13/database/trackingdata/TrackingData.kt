package co.aresid.book13.database.trackingdata

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

@Entity(tableName = DatabaseNames.Table.TrackingData.NAME)
data class TrackingData(
	
	@PrimaryKey(autoGenerate = true) @ColumnInfo(name = DatabaseNames.Table.TrackingData.Column.ID) val id: Long = 0,
	
	@ColumnInfo(name = DatabaseNames.Table.TrackingData.Column.BOOK_ID) val bookId: Long,
	
	@ColumnInfo(name = DatabaseNames.Table.TrackingData.Column.BOOK_TITLE) val bookTitle: String,
	
	@ColumnInfo(name = DatabaseNames.Table.TrackingData.Column.START_PAGE_COUNT) val startPageCount: Int,
	
	@ColumnInfo(name = DatabaseNames.Table.TrackingData.Column.FINISH_PAGE_COUNT) val finishPageCount: Int,
	
	@ColumnInfo(name = DatabaseNames.Table.TrackingData.Column.START_DATE) val startDate: Long,
	
	@ColumnInfo(name = DatabaseNames.Table.TrackingData.Column.FINISH_DATE) val finishDate: Long

)