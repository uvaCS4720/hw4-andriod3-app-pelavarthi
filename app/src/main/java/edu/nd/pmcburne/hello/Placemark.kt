package edu.nd.pmcburne.hello

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "placemarks")
data class Placemark(
    @PrimaryKey val id: Int,
    val name: String,
    val tags: String,
    val description: String,
    val latitude: Double,
    val longitude: Double
)
