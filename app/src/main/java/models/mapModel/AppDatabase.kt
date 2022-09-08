package models.mapModel

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import models.mapModel.entities.MarkerRepos
import models.mapModel.entities.PolygonRepos
import models.mapModel.entities.StateRepos

@Database(entities = [StateRepos::class, PolygonRepos::class, MarkerRepos::class], version = 1)
    //autoMigrations = [AutoMigration (from = 1, to = 2)])
abstract class AppDatabase : RoomDatabase() {
    abstract fun mapDao(): MapDao
}