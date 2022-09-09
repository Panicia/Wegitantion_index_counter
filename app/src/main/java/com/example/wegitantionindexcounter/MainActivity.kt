package com.example.wegitantionindexcounter

import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.preference.PreferenceManager
import com.example.wegitantionindexcounter.databinding.ActivityMainBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.osmdroid.api.IMapController
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.*
import org.osmdroid.config.Configuration
import viewModels.mapViewModel.MapViewModel
import views.mapView.MapOverlayHandler
import views.mapView.MapEventsHandler
import views.mapView.MapStateHandler
import views.mapView.buttons.MarkerAddAvailableBtn
import views.mapView.buttons.RotateMapBtn


class MainActivity : AppCompatActivity() {

    private val mapViewModel by viewModel<MapViewModel>()

    private val REQUEST_PERMISSIONS_REQUEST_CODE = 1

    private lateinit var map: MapView

    private lateinit var binding: ActivityMainBinding

    private lateinit var markersAdder: MapEventsHandler
    private lateinit var mapOverlayHandler: MapOverlayHandler
    private lateinit var rotateMapBtn: RotateMapBtn
    private lateinit var markerAddAvailableBtn: MarkerAddAvailableBtn
    private lateinit var mapStateHandler: MapStateHandler


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this))
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initAll()
    }

    override fun onResume() {
        super.onResume()
        mapStateHandler.loadMap(map, markersAdder)
        mapViewModel.mapStateLive.observe(this, Observer {
        })

        binding.buttonRotate.setOnClickListener {
            rotateMapBtn.pressButton()
        }
        binding.buttonMarkersAddAvailable.setOnClickListener {
            markerAddAvailableBtn.pressButton()
        }
        binding.buttonDeleteAll.setOnClickListener {
            mapOverlayHandler.deleteAll()
        }
        map.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapStateHandler.convertAndSaveMap(map)
        map.onPause()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        val permissionsToRequest = ArrayList<String>()
        var i = 0
        while (i < grantResults.size) {
            permissionsToRequest.add(permissions[i])
            i++
        }
        if (permissionsToRequest.size > 0) {
            ActivityCompat.requestPermissions(
                this,
                permissionsToRequest.toTypedArray(),
                REQUEST_PERMISSIONS_REQUEST_CODE)
        }
    }

    private fun initAll() {
        map = binding.mapView
        mapOverlayHandler = MapOverlayHandler(map)
        rotateMapBtn = RotateMapBtn(mapOverlayHandler, binding.buttonRotate)
        markerAddAvailableBtn = MarkerAddAvailableBtn(mapOverlayHandler, binding.buttonMarkersAddAvailable)
        markersAdder = MapEventsHandler(markerAddAvailableBtn, mapOverlayHandler)
        mapStateHandler = MapStateHandler(mapViewModel)
    }
}
