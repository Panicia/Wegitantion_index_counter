package com.example.wegitantionindexcounter

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import androidx.preference.PreferenceManager
import android.view.View
import android.widget.Button
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.wegitantionindexcounter.databinding.ActivityMainBinding
import org.osmdroid.api.IMapController
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.views.MapView
import org.osmdroid.config.Configuration.*
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.*
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay


class MainActivity : AppCompatActivity() {
    private val REQUEST_PERMISSIONS_REQUEST_CODE = 1

    private lateinit var map:MapView
    private lateinit var mapController:IMapController

    private lateinit var binding: ActivityMainBinding

    private lateinit var markersAdder : MarkersAdder
    private lateinit var mapOverlayHandler: MapOverlayHandler
    private lateinit var rotateMapBtn : RotateMapBtn
    private lateinit var markerAddAvailableBtn : MarkerAddAvailableBtn

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this))
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        map = binding.mapView
        mapOverlayHandler = MapOverlayHandler(map, this)
        rotateMapBtn = RotateMapBtn(mapOverlayHandler, binding.button2, this)
        markerAddAvailableBtn = MarkerAddAvailableBtn(mapOverlayHandler, binding.button3, this)
        markersAdder = MarkersAdder(markerAddAvailableBtn, mapOverlayHandler)
        setMapDefaults(map)
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
        }
        val mapEventsOverlay = MapEventsOverlay(markersAdder)
        map.overlays.add(mapEventsOverlay)
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

    private fun setMapDefaults(map:MapView) {
        map.setLayerType(View.LAYER_TYPE_HARDWARE, null)
        map.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE);

        map.setMultiTouchControls(true);
        mapController = map.controller;
        mapController.setZoom(10.0);

        val dm : DisplayMetrics = this.resources.displayMetrics
        val scaleBarOverlay = ScaleBarOverlay(map)
        scaleBarOverlay.setCentred(true)
        scaleBarOverlay.setScaleBarOffset(dm.widthPixels / 2, 10)
        map.overlays.add(scaleBarOverlay)
        val startPointAhrangelsk = GeoPoint(64.54008896758883, 40.51580601698074);
        mapController.setCenter(startPointAhrangelsk);
    }

    open class MapBtn(_mapOverlayHandler : MapOverlayHandler, _btn : Button, _context : Context) {
        protected val context = _context
        protected val rButton = _btn
        protected val mapOverlayHandler = _mapOverlayHandler

        var isEnabled = false
        get() {
            return field
        }
        open fun pressButton() {}
    }
    class RotateMapBtn(_mapOverlayHandler: MapOverlayHandler, _btn : Button, _context : Context) : MapBtn(_mapOverlayHandler, _btn, _context) {
        override fun pressButton() {
            if(isEnabled) {
                disableMapRotate()
                isEnabled = false
            }
            else {
                enableMapRotate()
                isEnabled = true
            }
        }
        private fun enableMapRotate() {
            mapOverlayHandler.setRotate()
            rButton.compoundDrawableTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.purple_500))
        }
        private fun disableMapRotate() {
            mapOverlayHandler.deleteRotate()
            rButton.compoundDrawableTintList = ColorStateList.valueOf(Color.argb(100,0,0,0))
        }
    }
    class MarkerAddAvailableBtn(_mapOverlayHandler:MapOverlayHandler, _btn : Button, _context : Context) : MapBtn(_mapOverlayHandler, _btn, _context) {
        override fun pressButton() {
            if(isEnabled) {
                disableMarkers()
                isEnabled = false
            }
            else {
                enableMarkers()
                isEnabled = true
            }
        }
        private fun enableMarkers() {
            rButton.compoundDrawableTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.purple_500))
        }
        private fun disableMarkers() {
            rButton.compoundDrawableTintList = ColorStateList.valueOf(Color.argb(100,0,0,0))
        }
    }
    class MarkersAdder(_button : MarkerAddAvailableBtn, _mapOverlayHandler : MapOverlayHandler) : MapEventsReceiver {
        private val button  = _button
        private val mapOverlayHandler = _mapOverlayHandler
        override fun singleTapConfirmedHelper(p: GeoPoint?): Boolean {
            if (button.isEnabled) {
                Log.d("TAG", "short ${p?.latitude} - ${p?.longitude}")
                mapOverlayHandler.setMarker(p)
            }
            return true
        }
        override fun longPressHelper(p: GeoPoint?): Boolean {
            Log.d("TAG", "long ${p?.latitude} - ${p?.longitude}")
            mapOverlayHandler.deleteLastMarker()
            return false
        }
    }
    class MapOverlayHandler(_map : MapView, _context: Context) {
        private val context = _context
        private val map = _map
        private var markersCounter = 0
        private val geoPointArray = ArrayList<GeoPoint>()
        private var polygon = Polygon()
        private val rotationGestureOverlay = RotationGestureOverlay(map)

        init {
            map.overlays.add(rotationGestureOverlay)
            rotationGestureOverlay.isEnabled = false
        }

        fun setRotate() {
            rotationGestureOverlay.isEnabled = true
        }
        fun deleteRotate() {
            rotationGestureOverlay.isEnabled = false
        }
        fun setMarker(p:GeoPoint?) {
            if(p != null) {
                val marker = Marker(map)
                marker.position = p
                marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                marker.icon = ContextCompat.getDrawable(context, R.drawable.geo_fill_icon_185595)
                marker.title = "marker $markersCounter\n lat: ${p.latitude}\n lon: ${p.longitude}"
                markersCounter++
                geoPointArray.add(p)
                map.overlays.add(marker)
                if(markersCounter > 2) {
                    deletePolygon()
                    createPolygon()
                }
                map.invalidate()
            }
        }
        private fun createPolygon() {
            if(markersCounter > 0) {
                geoPointArray.add(geoPointArray[0])
                polygon.fillPaint.color = Color.parseColor("#1EFFE70E")
                polygon.points = geoPointArray
                polygon.title = "Polygon 1"
                map.overlays.add(polygon)
                geoPointArray.removeAt(geoPointArray.size - 1)
            }
        }
        private fun deletePolygon() {
            for(i in map.overlays.size - 1 downTo 0) {
                if(map.overlays[i] is Polygon) {
                    map.overlays.removeAt(i)
                    break
                }
            }
        }
        fun deleteLastMarker() {
            if(markersCounter > 0){
                for (i in map.overlays.size - 1 downTo 0) {
                    if (map.overlays[i] is Marker) {
                        map.overlays.removeAt(i)
                        geoPointArray.removeAt(geoPointArray.size - 1)
                        break
                    }
                }
                if(markersCounter > 2) {
                    deletePolygon()
                    markersCounter--
                    if(markersCounter > 2) createPolygon()
                }
                else markersCounter--
                map.invalidate()
            }
        }
        fun deleteAll() {
            for(i in map.overlays.size - 1 downTo 0) {
                if(map.overlays[i] is Marker || map.overlays[i] is Polygon) {
                    map.overlays.removeAt(i)
                }
            }
            geoPointArray.clear()
            markersCounter = 0
            map.invalidate()
        }
    }
}
