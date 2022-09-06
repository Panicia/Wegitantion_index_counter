package models.mapModel

import androidx.room.Database
import androidx.room.RoomDatabase
import models.mapModel.entities.Marker
import models.mapModel.entities.Polygon
import models.mapModel.entities.State

@Database(entities = [State::class, Polygon::class, Marker::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun mapDao(): MapDao
}