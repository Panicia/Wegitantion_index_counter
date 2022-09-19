package views.mapView

import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.wegitantionindexcounter.R
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.infowindow.InfoWindow
import viewModels.mapViewModel.MyPolygon

class MarkersManager(
    private val map: MapView) {

    private lateinit var onMarkerDragListener: MyOnMarkerDragListener
    private val markersArray = ArrayList<Marker>()

    fun setMyOnMarkerDragListener(myOnMarkerDragListener: MyOnMarkerDragListener) {
        onMarkerDragListener = myOnMarkerDragListener
    }


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

    fun hideMarkersOfPolygon(polygon: MyPolygon) {
        deleteAllMarkers()
    }

    fun deleteAllMarkers() {
        for(i in map.overlays.size - 1 downTo 0) {
            if(map.overlays[i] is Marker) {
                map.overlays.removeAt(i)
            }
        }
        markersArray.clear()
        map.invalidate()
    }

    fun deleteMarker(marker: Marker) {
        if(markersArray.contains(marker) && map.overlays.contains(marker)) {
            markersArray.remove(marker)
            map.overlays.remove(marker)
            map.invalidate()
        }
    }

    private fun setMarkerDefaults(marker: Marker, p: GeoPoint, markerNumber: Int) {
        marker.position = p
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        marker.icon = ContextCompat.getDrawable(map.context, R.drawable.geo_fill_icon_185595)
        marker.title = "Point ${markerNumber + 1}"
        marker.isDraggable = true
        marker.setOnMarkerDragListener(onMarkerDragListener)
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