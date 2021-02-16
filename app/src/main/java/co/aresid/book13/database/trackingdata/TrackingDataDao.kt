package co.aresid.book13.database.trackingdata

import androidx.lifecycle.LiveData
import androidx.room.*

/**
 *    Created on: 02.09.2020
 *    For Project: book13
 *    Author: René Spies
 *    Copyright: © 2020 ARES ID
 */

@Dao
interface TrackingDataDao {

    /**
     * Retrieves a [LiveData] instance with the type [List] with the type [TrackingData]
     * which holds all currently stored [TrackingData].
     *
     * @return [LiveData] with type [List] with type [TrackingData].
     */
    @Query("SELECT * FROM tracking_data")
    fun getAllLiveData(): LiveData<List<TrackingData>>

    /**
     * Retrieves a row from the table in the form of [TrackingData] where [trackingId] is found.
     *
     * @return [TrackingData] representing a row in the table.
     */
    @Query("SELECT * FROM tracking_data WHERE id = :trackingId")
    suspend fun getById(trackingId: Long): TrackingData?

    /**
     * Inserts a new row into the table from the [data].
     * Replaces all old data if a [TrackingData] with this primary key ([TrackingData.id]) already exists.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(data: TrackingData)

    @Delete
    suspend fun delete(data: TrackingData)

}