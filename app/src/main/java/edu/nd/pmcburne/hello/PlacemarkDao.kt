package edu.nd.pmcburne.hello

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PlacemarkDao {
    @Query("SELECT * FROM placemarks")
    suspend fun getAll(): List<Placemark>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(placemarks: List<Placemark>)
}
