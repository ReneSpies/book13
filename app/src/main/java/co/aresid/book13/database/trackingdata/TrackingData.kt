package co.aresid.book13.database.trackingdata

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import co.aresid.book13.database.DatabaseNames
import co.aresid.book13.database.bookdata.BookData

/**
 *    Created on: 02.09.2020
 *    For Project: book13
 *    Author: René Spies
 *    Copyright: © 2020 ARES ID
 */

@Entity(
    tableName = DatabaseNames.Table.TrackingData.NAME,
    // Create a ForeignKey relation to the BookData class' table
    foreignKeys = [ForeignKey(
        entity = BookData::class, // Relating table
        parentColumns = [DatabaseNames.Table.BookData.Column.ID], // Relating column from the relating table
        childColumns = [DatabaseNames.Table.TrackingData.Column.BOOK_ID], // Relating column from this table
        onDelete = ForeignKey.CASCADE // Deletes the parent's row if this one is deleted
    )]
)
data class TrackingData(

    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = DatabaseNames.Table.TrackingData.Column.ID) val id: Long = 0,

    @ColumnInfo(name = DatabaseNames.Table.TrackingData.Column.BOOK_ID) val bookId: Long,

    @ColumnInfo(name = DatabaseNames.Table.TrackingData.Column.BOOK_TITLE) val bookTitle: String,

    @ColumnInfo(name = DatabaseNames.Table.TrackingData.Column.START_PAGE_COUNT) val startPageCount: Int,

    @ColumnInfo(name = DatabaseNames.Table.TrackingData.Column.FINISH_PAGE_COUNT) val finishPageCount: Int,

    @ColumnInfo(name = DatabaseNames.Table.TrackingData.Column.START_DATE) val startDate: Long,

    @ColumnInfo(name = DatabaseNames.Table.TrackingData.Column.FINISH_DATE) val finishDate: Long

)