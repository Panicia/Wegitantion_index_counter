package com.example.wegitantionindexcounter

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import androidx.preference.PreferenceManager
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.wegitantionindexcounter.databinding.ActivityMainBinding
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.osmdroid.api.IMapController
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.views.MapView
import org.osmdroid.config.Configuration.*
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.overlay.*
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay
import org.osmdroid.views.overlay.infowindow.InfoWindow
import kotlin.math.pow


class MainActivity : AppCompatActivity() {
    private val REQUEST_PERMISSIONS_REQUEST_CODE = 1

    private lateinit var map:MapView
    private lateinit var mapController:IMapController

    private lateinit var binding: ActivityMainBinding

    private lateinit var markersAdder : MarkersAdder
    private lateinit var mapOverlayHandler: MapOverlayHandler
    private lateinit var rotateMapBtn : RotateMapBtn
    private lateinit var markerAddAvailableBtn : MarkerAddAvailableBtn
    //private lateinit var dynamicAreasView : DynamicAreasView


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
        //dynamicAreasView = DynamicAreasView(binding.bottomAppBar, this)
        val mapEventsOverlay = MapEventsOverlay(markersAdder)
        map.overlays.add(mapEventsOverlay)
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
        map.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE)
        map.setMultiTouchControls(true)
        mapController = map.controller
        mapController.setZoom(10.0)
        map.zoomController.setVisibility(CustomZoomButtonsController.Visibility.NEVER)
        val dm : DisplayMetrics = this.resources.displayMetrics
        val scaleBarOverlay = ScaleBarOverlay(map)
        scaleBarOverlay.setCentred(true)
        scaleBarOverlay.setScaleBarOffset(dm.widthPixels / 2, 10)
        map.overlays.add(scaleBarOverlay)
        val startPointAhrangelsk = GeoPoint(64.54008896758883, 40.51580601698074)
        mapController.setCenter(startPointAhrangelsk)
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
                mapOverlayHandler.setMarker(p)
            }
            return true
        }
        override fun longPressHelper(p: GeoPoint?): Boolean {
            if (button.isEnabled) {
                //mapOverlayHandler.deleteLastMarker()
            }
            return false
        }
    }
    class DynamicAreasView(_view: BottomAppBar, _context: Context) {
        private val view = _view
        private val context = _context
        private val viewArray = ArrayList<View>()

        init {
            //view.layoutParams.height = 0
            view.setBackgroundColor(Color.argb(100,0,0,0))
        }

        fun addMarkerInView(marker: Marker) {
            val markerView = View(context)
            //view.addView()
        }
    }
    class MapOverlayHandler(_map : MapView, _context: Context) : Marker.OnMarkerDragListener {
        private val context = _context
        private val map = _map
        private var markersCounter = 0
        private val markersArray = ArrayList<Marker>()
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
                setMarkerDefaults(marker, p)
                placeMarker(marker)
                if(markersCounter > 2) {
                    deletePolygon()
                    createPolygon()
                }
                map.invalidate()
            }
        }
        private fun placeMarker(marker: Marker) {
            if(markersCounter > 2) {
                //val iter = checkNearEdge(marker.position)
                markersArray.add(marker)
            }
            else
                markersArray.add(marker)
            map.overlays.add(marker)
            markersCounter++
        }
        private fun setMarkerDefaults(marker: Marker, p: GeoPoint) {
            marker.position = p
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
            marker.icon = ContextCompat.getDrawable(context, R.drawable.geo_fill_icon_185595)
            marker.title = "lat: ${p.latitude}\nlon: ${p.longitude}"
            marker.isDraggable = true
            marker.setOnMarkerDragListener(this)
            marker.dragOffset = 6f
            val infoWindow = MarkerWindow(map, marker, this)
            marker.infoWindow = infoWindow
        }
        private fun makeGeoPointsArray() : ArrayList<GeoPoint> {
            val array = ArrayList<GeoPoint>()
            for(i in 0 until markersArray.size) {
                val geoPoint = GeoPoint(markersArray[i].position)
                array.add(geoPoint)
            }
            array.add(array[0])
            return array
        }
        private fun createPolygon() {
            if(markersCounter > 0) {
                val geoPointArray = makeGeoPointsArray()
                polygon.fillPaint.color = Color.parseColor("#1EFFE70E")
                polygon.points = geoPointArray
                polygon.title = "Polygon 1"
                map.overlays.add(0, polygon)
            }
        }
        private fun checkNearEdge(geoPoint: GeoPoint) : Int {
            var minDistanceIter = 0
            val list = makeGeoPointsArray()
            var minDist : Double = getDistance(geoPoint, list[0])
            for(i in 1 until list.size - 1) {
                val dist = getDistance(geoPoint, list[i])
                if(dist < minDist) {
                    minDist = dist
                    minDistanceIter = i
                }
            }
            val len1 : Double
            val len2 : Double
            if(minDistanceIter == 0) {
                len1 = getDistance(geoPoint, list[list.size - 2])
                len2 = getDistance(geoPoint, list[1])
                if(len1 > len2) {
                    return minDistanceIter + 1
                }
                else {
                    return list.size - 1
                }
            }
            else {
                len1 = getDistance(geoPoint, list[minDistanceIter - 1])
                len2 = getDistance(geoPoint, list[minDistanceIter + 1])
                if(len1 > len2) {
                    return minDistanceIter + 1
                }
                else {
                    return minDistanceIter
                }
            }
        }
        private fun getDistance(target: GeoPoint, geoPoint1: GeoPoint) : Double {
            val len1 = ((target.longitude - geoPoint1.longitude).pow(2.0) + (target.latitude - geoPoint1.latitude).pow(2.0)).pow(0.5)
            return len1
        }
        private fun deletePolygon() {
            if(map.overlays.contains(polygon)) {
                map.overlays.remove(polygon)
            }
        }
        fun deleteMarker(marker:Marker) {
            if(markersArray.contains(marker) && map.overlays.contains(marker)) {
                markersArray.remove(marker)
                map.overlays.remove(marker)
                redrawPolygonIfNeeded()
            }
        }
        private fun redrawPolygonIfNeeded() {
            if(markersCounter > 2) {
                deletePolygon()
                markersCounter--
                if(markersCounter > 2) createPolygon()
            }
            else markersCounter--
            map.invalidate()
        }
        fun deleteLastMarker() {
            if(markersCounter > 0) {
                for (i in map.overlays.size - 1 downTo 0) {
                    if (map.overlays[i] is Marker) {
                        map.overlays.removeAt(i)
                        markersArray.removeAt(markersArray.size - 1)
                        break
                    }
                }
                redrawPolygonIfNeeded()
            }
        }
        fun deleteAll() {
            for(i in map.overlays.size - 1 downTo 0) {
                if(map.overlays[i] is Marker) {
                    map.overlays.removeAt(i)
                }
            }
            deletePolygon()
            InfoWindow.closeAllInfoWindowsOn(map)
            markersArray.clear()
            markersCounter = 0
            map.invalidate()
        }
        override fun onMarkerDrag(marker: Marker?) {
            deletePolygon()
            createPolygon()
        }
        override fun onMarkerDragStart(marker: Marker?) {
            marker?.icon = ContextCompat.getDrawable(context, R.drawable.geo_fill_icon_red)
        }
        override fun onMarkerDragEnd(marker: Marker?) {
            marker?.icon = ContextCompat.getDrawable(context, R.drawable.geo_fill_icon_185595)
        }
    }
    class MarkerWindow(mapView: MapView, _marker: Marker, _mapOverlayHandler: MapOverlayHandler) : InfoWindow(R.layout.marker_layout, mapView) {
        private val mapOverlayHandler = _mapOverlayHandler
        private val marker = _marker
        private val map = mapView
        override fun onOpen(item: Any?) {
            closeAllInfoWindowsOn(map)
            val deleteButton = mView.findViewById<Button>(R.id.delete_button)
            val textView = mView.findViewById<TextView>(R.id.text_view)
            textView.text = marker.title

            deleteButton.setOnClickListener {
                mapOverlayHandler.deleteMarker(marker)
                closeAllInfoWindowsOn(map)
            }
            mView.setOnClickListener {
                close()
            }
        }
        override fun onClose() {

        }
    }
}
