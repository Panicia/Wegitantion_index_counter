package models.mapModel.entities

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "polygons", foreignKeys = [ForeignKey(
    entity = StateRepos::class,
    childColumns = ["stateId"],
    parentColumns = ["id"]
)])
data class PolygonRepos(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val name: String,
    val image: Bitmap,
    val stateId: Long
)