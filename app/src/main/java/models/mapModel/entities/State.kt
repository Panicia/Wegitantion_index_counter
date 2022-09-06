package models.mapModel.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "states")
data class State(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val zoom: Double,
    val centerLat: Double,
    val centerLon: Double
)