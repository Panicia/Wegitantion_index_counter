package views.mapView.mapOverlays

import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.wegitantionindexcounter.R
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.infowindow.InfoWindow
import views.mapView.myClasses.MyPolygon

class MarkersManager(
    private val map: MapView,
    private val mapOverlayHandler: MapOverlayHandler
    ) {

    private val markersArray = ArrayList<Marker>()

    fun showMarkersOfPolygon(polygon: MyPolygon) {
        if(polygon.actualPoints != null) {
            for(i in 0 until polygon.actualPoints.count()) {
                val marker = Marker(map)
                setMarkerDefaults(marker, polygon.actualPoints[i], i)
                markersArray.add(marker)
                map.overlays.add(marker)
            }
        }
    }

    fun hideMarkersOfActivePolygon() {
        deleteAllMarkers()
    }

    fun redrawMarkersOfPolygon(polygon: MyPolygon) {
        hideMarkersOfActivePolygon()
        showMarkersOfPolygon(polygon)
        map.invalidate()
    }

    fun returnPoints(): ArrayList<GeoPoint> {
        val points = ArrayList<GeoPoint>()
        for(marker in markersArray) {
            points.add(marker.position)
        }
        return points
    }

    fun deleteMarker(marker: Marker) {
        if(markersArray.contains(marker) && map.overlays.contains(marker)) {
            markersArray.remove(marker)
            map.overlays.remove(marker)
            map.invalidate()
        }
    }

    private fun deleteAllMarkers() {
        for(i in map.overlays.size - 1 downTo 0) {
            if(map.overlays[i] is Marker) {
                map.overlays.removeAt(i)
            }
        }
        markersArray.clear()
        map.invalidate()
    }

    private fun setMarkerDefaults(marker: Marker, p: GeoPoint, markerNumber: Int) {
        marker.position = p
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        marker.icon = ContextCompat.getDrawable(map.context, R.drawable.geo_fill_icon_185595)
        marker.title = "Point ${markerNumber + 1}"
        marker.isDraggable = true
        marker.setOnMarkerDragListener(mapOverlayHandler.getOnMarkerDrugListener())
        marker.dragOffset = 6f
        val infoWindow = MarkerWindow(map, marker)
        marker.infoWindow = infoWindow
    }

    inner class MarkerWindow(
        private val map: MapView,
        private val marker: Marker

    ) : InfoWindow(R.layout.marker_layout, map) {

        override fun onOpen(item: Any?) {
            closeAllInfoWindowsOn(map)
            val deleteButton = mView.findViewById<Button>(R.id.delete_button)
            val textView = mView.findViewById<TextView>(R.id.text_view)
            textView.text = getMarkerPos()

            deleteButton.setOnClickListener {
                deleteMarker(marker)
                mapOverlayHandler.redrawActivePolygon()
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