package models.mapModel

import androidx.room.Database
import androidx.room.RoomDatabase
import models.mapModel.entities.MarkerRepos
import models.mapModel.entities.PolygonRepos
import models.mapModel.entities.StateRepos

@Database(entities = [StateRepos::class, PolygonRepos::class, MarkerRepos::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun mapDao(): MapDao
}