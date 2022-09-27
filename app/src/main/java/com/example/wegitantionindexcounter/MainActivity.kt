package com.example.wegitantionindexcounter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.preference.PreferenceManager
import com.example.wegitantionindexcounter.databinding.ActivityMainBinding
import com.squareup.picasso.Picasso
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.osmdroid.views.MapView
import org.osmdroid.config.Configuration
import viewModels.mapViewModel.MapViewModel
import views.mapView.mapOverlays.MapOverlayHandler
import views.mapView.MapEventsHandler
import views.mapView.MapStateHandler
import views.mapView.myClasses.PictureTarget
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

        mapStateHandler.loadMap(map, markersAdder)

        /*mapViewModel.getMapStateLive().observe(this) {
            mapStateHandler.loadMap(map, markersAdder)
        }*/

        mapViewModel.getPictureResponseLive().observe(this) {
            val pictureTarget = PictureTarget(mapOverlayHandler, mapViewModel.getPictureResponseLive().value!!)
            Picasso.get().load(mapViewModel.getPictureResponseLive().value?.url).into(pictureTarget)
        }
    }

    override fun onResume() {
        super.onResume()

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
        mapOverlayHandler = MapOverlayHandler(map, mapViewModel)
        rotateMapBtn = RotateMapBtn(mapOverlayHandler, binding.buttonRotate)
        markerAddAvailableBtn = MarkerAddAvailableBtn(mapOverlayHandler, binding.buttonMarkersAddAvailable)
        markersAdder = MapEventsHandler(markerAddAvailableBtn, mapOverlayHandler)
        mapStateHandler = MapStateHandler(mapViewModel, mapOverlayHandler)
    }
}
