package com.example.wegitantionindexcounter.app.di

import android.app.Application
import androidx.room.Room
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import models.mapModel.*
import okhttp3.Cache
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import viewModels.mapViewModel.MapViewModel

val viewModelModule = module {
    viewModel { MapViewModel(get(), get()) }
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


val apiRequestHandlerModule = module {
    fun provideApiRequestHandler(mapApi: MapApi): ApiRequestHandler {
        return ApiRequestHandler(mapApi)
    }

    single { provideApiRequestHandler(get()) }
}


val apiModule = module {
    fun provideUserApi(retrofit: Retrofit): MapApi {
        return retrofit.create(MapApi::class.java)
    }

    single { provideUserApi(get()) }
}


val netModule = module {

    fun provideCache(application: Application): Cache {
        val cacheSize = 10 * 1024 * 1024
        return Cache(application.cacheDir, cacheSize.toLong())
    }

    fun provideHttpClient(cache: Cache): OkHttpClient {
        val okHttpClientBuilder = OkHttpClient.Builder()
            .cache(cache)

        return okHttpClientBuilder.build()
    }

    fun provideGson(): Gson {
        return GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.IDENTITY).create()
    }

    fun provideRetrofit(factory: Gson, client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://192.168.0.107:5000")
            .addConverterFactory(GsonConverterFactory.create(factory))
            .client(client)
            .build()
    }

    single { provideCache(androidApplication()) }
    single { provideHttpClient(get()) }
    single { provideGson() }
    single { provideRetrofit(get(), get()) }

}