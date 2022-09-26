package models.mapModel.dbEntities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "states")
data class StateRepos(
    @PrimaryKey val id: Long,
    val zoom: Double,
    val centerLat: Double,
    val centerLon: Double
)