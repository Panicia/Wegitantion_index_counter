package views.mapView.mapOverlays

import androidx.core.content.ContextCompat
import com.example.wegitantionindexcounter.R
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

class MyOnMarkerDragListener (
    private val map: MapView,
    private val mapOverlayHandler: MapOverlayHandler

    ): Marker.OnMarkerDragListener {

    override fun onMarkerDrag(marker: Marker?) {
            mapOverlayHandler.redrawActivePolygon()
    }

    override fun onMarkerDragStart(marker: Marker?) {
        marker?.icon = ContextCompat.getDrawable(map.context, R.drawable.geo_fill_icon_red)
    }

    override fun onMarkerDragEnd(marker: Marker?) {
        marker?.icon = ContextCompat.getDrawable(map.context, R.drawable.geo_fill_icon_185595)
    }
}