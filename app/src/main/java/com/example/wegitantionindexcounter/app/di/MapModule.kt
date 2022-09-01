package com.example.wegitantionindexcounter.app.di

import android.view.LayoutInflater
import com.example.wegitantionindexcounter.MainActivity
import com.example.wegitantionindexcounter.databinding.ActivityMainBinding
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import org.osmdroid.views.MapView
import viewModels.MapViewModel

val viewModelModule = module {
    viewModel { MapViewModel() }
}

val mapModule = module {

}
