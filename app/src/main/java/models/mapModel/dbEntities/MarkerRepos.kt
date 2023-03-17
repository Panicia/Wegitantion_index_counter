package models.mapModel.dbEntities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.PrimaryKey

@Entity(tableName = "markers", foreignKeys = [ForeignKey(
    entity = PolygonRepos::class,
    childColumns = ["polId"],
    parentColumns = ["id"],
    onDelete = CASCADE
)])
data class MarkerRepos(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val lat: Double,
    val lon: Double,
    val polId: Long
)