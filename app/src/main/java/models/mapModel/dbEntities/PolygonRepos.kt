package models.mapModel.dbEntities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.PrimaryKey

@Entity(tableName = "polygons", foreignKeys = [ForeignKey(
    entity = StateRepos::class,
    childColumns = ["stateId"],
    parentColumns = ["id"],
    onDelete = CASCADE
)])
data class PolygonRepos(
    @PrimaryKey val id: Long,
    val name: String,
    val imagePath: String?,
    val stateId: Long
)