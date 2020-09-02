package co.aresid.book13.database.trackingdata

import androidx.lifecycle.LiveData
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
interface TrackingDataDao {

    @Query("SELECT * FROM tracking_data")
    fun getAllLiveData(): LiveData<List<TrackingData>>

    @Query("SELECT * FROM tracking_data WHERE id = :trackingId")
    suspend fun get(trackingId: Long): TrackingData?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(data: TrackingData)

}