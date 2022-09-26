package models.mapModel

import androidx.room.Database
import androidx.room.RoomDatabase
import models.mapModel.dbEntities.MarkerRepos
import models.mapModel.dbEntities.PolygonRepos
import models.mapModel.dbEntities.StateRepos

@Database(entities = [StateRepos::class, PolygonRepos::class, MarkerRepos::class], version = 1)
    //autoMigrations = [AutoMigration (from = 1, to = 2)])
abstract class AppDatabase : RoomDatabase() {
    abstract fun mapDao(): MapDao
}