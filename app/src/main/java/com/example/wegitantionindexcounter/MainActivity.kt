package com.example.wegitantionindexcounter

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.ColorSpace
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.KeyEvent
import android.view.MotionEvent
import androidx.preference.PreferenceManager
import android.view.View
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.toColor
import androidx.core.graphics.toColorLong
import com.example.wegitantionindexcounter.databinding.ActivityMainBinding
import org.osmdroid.api.IMapController
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.views.MapView
import org.osmdroid.config.Configuration.*
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.events.MapListener
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.ScaleBarOverlay
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay


class MainActivity : AppCompatActivity() {
    private val REQUEST_PERMISSIONS_REQUEST_CODE = 1

    private lateinit var map:MapView
    private lateinit var mapController:IMapController

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this))
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        map = binding.mapView
        setMapDefaults(map)

    }

    override fun onResume() {
        super.onResume()
        val rotateBtn = RotateMapBtn(map, binding.button2, this)
        binding.button2.setOnClickListener {
            rotateBtn.pressButton()
        }
        val markerBtn = MarkerMapBtn(map, binding.button3, this)
        binding.button3.setOnClickListener {
            markerBtn.pressButton()
        }
        //val mapEventsReceiver = MapEventsReceiverImpl()
        val mapEventsOverlay = MapEventsOverlay(markerBtn)
        map.overlays.add(mapEventsOverlay)
        map.onResume()
    }

    class MapEventsReceiverImpl(_map:MapView) : MapEventsReceiver {

        override fun singleTapConfirmedHelper(p: GeoPoint?): Boolean {
            Log.d("TAG", "${p?.latitude} - ${p?.longitude}")
            return true
        }

        override fun longPressHelper(p: GeoPoint?): Boolean {
            Log.d("TAG", "${p?.latitude} - ${p?.longitude}")
            return false
        }
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

    open class MapBtn(_map : MapView, _btn : Button, _context : Context) {
        protected val context = _context
        protected val rButton = _btn
        protected val map = _map
        var isEnabled = false
        get() {
            return field
        }
        open fun pressButton() {

        }
    }
    class RotateMapBtn(_map : MapView, _btn : Button, _context : Context) : MapBtn(_map, _btn, _context) {
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
            val rotationGestureOverlay = RotationGestureOverlay(map)
            rotationGestureOverlay.isEnabled
            map.overlays.add(rotationGestureOverlay)
            rButton.compoundDrawableTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.purple_500))
        }
        private fun disableMapRotate() {
            map.overlays.removeAt(map.overlays.size - 1)
            rButton.compoundDrawableTintList = ColorStateList.valueOf(Color.argb(100,0,0,0))
        }
    }
    class MarkerMapBtn(_map : MapView, _btn : Button, _context : Context) : MapEventsReceiver, MapBtn(_map, _btn, _context) {
        var markersCounter = 0
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
        override fun singleTapConfirmedHelper(p: GeoPoint?): Boolean {
            if (isEnabled) {
                Log.d("TAG", "short ${p?.latitude} - ${p?.longitude}")
                setMarker(p)
            }
            return true
        }
        override fun longPressHelper(p: GeoPoint?): Boolean {
            Log.d("TAG", "long ${p?.latitude} - ${p?.longitude}")
            return false
        }
        private fun enableMarkers() {
            rButton.compoundDrawableTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.purple_500))
        }
        private fun disableMarkers() {
            rButton.compoundDrawableTintList = ColorStateList.valueOf(Color.argb(100,0,0,0))
        }
        private fun setMarker(p:GeoPoint?) {
            val marker = Marker(map)
            marker.position = p
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER)
            marker.icon = ContextCompat.getDrawable(context, org.osmdroid.library.R.drawable.osm_ic_follow_me_on)
            marker.title = "marker $markersCounter\n lat: ${p?.latitude} lon: ${p?.longitude}"
            markersCounter ++
            map.overlays.add(marker)
            map.invalidate()
        }
    }
}
