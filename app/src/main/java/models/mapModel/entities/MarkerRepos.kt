package models.mapModel.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "markers", foreignKeys = [ForeignKey(
    entity = PolygonRepos::class,
    childColumns = ["polId"],
    parentColumns = ["id"]
)])
data class MarkerRepos(
    @PrimaryKey(autoGenerate = true) val id: Long?,
    val lat: Double,
    val lon: Double,
    val polId: Long
)