package views.mapView

import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.wegitantionindexcounter.R
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.infowindow.InfoWindow

class MarkersManager(
    private val map: MapView,
    private val polygonHandler: PolygonsManager

    ): Marker.OnMarkerDragListener {

    private var markersCounter = 0
    private val markersArray = ArrayList<Marker>()

    override fun onMarkerDrag(marker: Marker?) {
        polygonHandler.deletePolygon()
        polygonHandler.createNewPolygon(markersArray)
    }
    override fun onMarkerDragStart(marker: Marker?) {
        marker?.icon = ContextCompat.getDrawable(map.context, R.drawable.geo_fill_icon_red)
    }
    override fun onMarkerDragEnd(marker: Marker?) {
        marker?.icon = ContextCompat.getDrawable(map.context, R.drawable.geo_fill_icon_185595)
    }

    fun createMarker(p: GeoPoint) : Marker {
        val marker = Marker(map)
        setMarkerDefaults(marker, p)
        markersArray.add(marker)
        map.overlays.add(marker)
        markersCounter++
        if(markersCounter > 2) {
            polygonHandler.deletePolygon()
            polygonHandler.createNewPolygon(markersArray)
        }
        map.invalidate()
        return marker
    }

    fun deleteAllMarkers() {
        for(i in map.overlays.size - 1 downTo 0) {
            if(map.overlays[i] is Marker) {
                map.overlays.removeAt(i)
            }
        }
        markersArray.clear()
        markersCounter = 0
    }

    fun deleteMarker(marker: Marker) {
        if(markersArray.contains(marker) && map.overlays.contains(marker)) {
            markersArray.remove(marker)
            map.overlays.remove(marker)
            polygonHandler.redrawPolygonIfNeeded(markersArray)
            markersCounter--
            map.invalidate()
        }
    }

    private fun setMarkerDefaults(marker: Marker, p: GeoPoint) {
        marker.position = p
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        marker.icon = ContextCompat.getDrawable(map.context, R.drawable.geo_fill_icon_185595)
        marker.title = "Point ${markersCounter + 1}"
        marker.isDraggable = true
        marker.setOnMarkerDragListener(this)
        marker.dragOffset = 6f
        val infoWindow = MarkerWindow(map, marker)
        marker.infoWindow = infoWindow
    }

    inner class MarkerWindow(
        private val map: MapView,
        private val marker: Marker,

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