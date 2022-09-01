package com.example.wegitantionindexcounter

import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.preference.PreferenceManager
import com.example.wegitantionindexcounter.databinding.ActivityMainBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.osmdroid.api.IMapController
import org.osmdroid.config.Configuration.*
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.*
import trash.DynamicAreasView
import viewModels.MapViewModel


class MainActivity : AppCompatActivity() {

    private val mapViewModel by viewModel<MapViewModel>()

    private val REQUEST_PERMISSIONS_REQUEST_CODE = 1

    private lateinit var map : MapView
    private lateinit var mapController : IMapController

    private lateinit var binding : ActivityMainBinding

    private lateinit var markersAdder : MarkersAdder
    private lateinit var mapOverlayHandler : MapOverlayHandler
    private lateinit var rotateMapBtn : RotateMapBtn
    private lateinit var markerAddAvailableBtn : MarkerAddAvailableBtn
    private lateinit var dynamicAreasView : DynamicAreasView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initAll()
    }

    override fun onResume() {
        super.onResume()
        binding.button2.setOnClickListener {
            rotateMapBtn.pressButton()
        }
        binding.button3.setOnClickListener {
            markerAddAvailableBtn.pressButton()
        }
        binding.button4.setOnClickListener {
            mapOverlayHandler.deleteAll()
            dynamicAreasView.hideSheet()
        }
        map.onResume()
    }

    override fun onPause() {
        super.onPause()
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
        //dynamicAreasView = DynamicAreasView(binding.standardBottomSheet, binding.listView1, this)
        mapOverlayHandler = MapOverlayHandler(map, this)
        rotateMapBtn = RotateMapBtn(mapOverlayHandler, binding.button2, this)
        markerAddAvailableBtn = MarkerAddAvailableBtn(mapOverlayHandler, binding.button3, this)
        markersAdder = MarkersAdder(markerAddAvailableBtn, mapOverlayHandler)
        mapNecessary(map)
        setMapDefaults(map)
    }

    fun mapNecessary(map : MapView) {
        val mapEventsOverlay = MapEventsOverlay(markersAdder)
        map.overlays.add(mapEventsOverlay)
        mapController = map.controller
        mapController.setZoom(10.0)
        val startPointAhrangelsk = GeoPoint(64.54008896758883, 40.51580601698074)
        mapController.setCenter(startPointAhrangelsk)
    }

    private fun setMapDefaults(map : MapView) {
        map.setLayerType(View.LAYER_TYPE_HARDWARE, null)
        map.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE)
        map.setMultiTouchControls(true)
        map.zoomController.setVisibility(CustomZoomButtonsController.Visibility.NEVER)
        val dm : DisplayMetrics = this.resources.displayMetrics
        val scaleBarOverlay = ScaleBarOverlay(map)
        scaleBarOverlay.setCentred(true)
        scaleBarOverlay.setScaleBarOffset(dm.widthPixels / 2, 10)
        map.overlays.add(scaleBarOverlay)
    }
}
