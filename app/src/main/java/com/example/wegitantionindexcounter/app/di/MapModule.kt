package com.example.wegitantionindexcounter.app.di

import android.app.Application
import androidx.room.Room
import models.mapModel.AppDatabase
import models.mapModel.MapDao
import models.mapModel.MapRepository
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import viewModels.mapViewModel.MapViewModel

val viewModelModule = module {
    viewModel { MapViewModel(get()) }
}

val databaseModule = module {

    fun provideDatabase(application: Application): AppDatabase {
        return Room.databaseBuilder(application, AppDatabase::class.java, "map_database")
            .allowMainThreadQueries()
            .build()
    }

    fun provideDao(database: AppDatabase): MapDao {
        return database.mapDao()
    }

    single { provideDatabase(androidApplication()) }
    single { provideDao(get()) }
}

val repositoryModule = module {
    fun provideRepository(mapDao: MapDao): MapRepository {
        return MapRepository(mapDao)
    }

    single { provideRepository(get()) }
}
