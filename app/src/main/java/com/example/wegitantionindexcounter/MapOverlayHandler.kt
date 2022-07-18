package com.example.wegitantionindexcounter

import android.content.Context
import android.graphics.Color
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polygon
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay
import org.osmdroid.views.overlay.infowindow.InfoWindow
import kotlin.math.pow

class MapOverlayHandler(_map : MapView, _context: Context, _dynamicAreasView: DynamicAreasView) : Marker.OnMarkerDragListener {
    private val dynamicAreasView = _dynamicAreasView
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
    fun setMarker(p: GeoPoint?) {
        if(p != null) {
            val marker = Marker(map)
            setMarkerDefaults(marker, p)
            placeMarker(marker)
            if(markersCounter > 2) {
                deletePolygon()
                createPolygon()
            }
            map.invalidate()
            dynamicAreasView.addMarkerInView(marker)
        }
    }
    private fun placeMarker(marker: Marker) {
        if(markersCounter > 2) {
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
        marker.title = "Point ${markersCounter + 1}"
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
    fun deleteMarker(marker: Marker) {
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
    class MarkerWindow(mapView: MapView, _marker: Marker, _mapOverlayHandler: MapOverlayHandler) : InfoWindow(R.layout.marker_layout, mapView) {
        private val mapOverlayHandler = _mapOverlayHandler
        private val marker = _marker
        private val map = mapView
        override fun onOpen(item: Any?) {
            closeAllInfoWindowsOn(map)
            val deleteButton = mView.findViewById<Button>(R.id.delete_button)
            val textView = mView.findViewById<TextView>(R.id.text_view)
            textView.text = getMarkerPos()

            deleteButton.setOnClickListener {
                mapOverlayHandler.deleteMarker(marker)
                closeAllInfoWindowsOn(map)
            }
            mView.setOnClickListener {
                close()
            }
        }
        private fun getMarkerPos(): String {
            return "lat: ${marker.position.latitude}\nlon: ${marker.position.longitude}"
        }
        override fun onClose() {

        }
    }
}
