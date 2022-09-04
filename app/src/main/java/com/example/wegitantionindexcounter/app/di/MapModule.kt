package com.example.wegitantionindexcounter.app.di

import models.MapRepository
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import viewModels.MapViewModel

val viewModelModule = module {
    viewModel { MapViewModel(get()) }
}

val repositoryModule = module {
    fun provideRepository(): MapRepository {
        return MapRepository()
    }

    single { provideRepository() }
}
