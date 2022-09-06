package models.mapModel.entities

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "polygons", foreignKeys = [ForeignKey(
    entity = State::class,
    childColumns = ["stateId"],
    parentColumns = ["id"]
)])
data class Polygon(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val name: String,
    val image: Bitmap,
    val stateId: Long
)