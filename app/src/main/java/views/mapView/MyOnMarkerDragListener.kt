package views.mapView

import androidx.core.content.ContextCompat
import com.example.wegitantionindexcounter.R
import org.osmdroid.views.overlay.Marker

class MyOnMarkerDragListener (
    val markersManager: MarkersManager,
    val polygonsManager: PolygonsManager
    ): Marker.OnMarkerDragListener {

    private var markersCounter = 0
    private val markersArray = ArrayList<Marker>()

    override fun onMarkerDrag(marker: Marker?) {
        deletePolygon()
        polygonHandler.createNewPolygon(markersArray)
    }
    override fun onMarkerDragStart(marker: Marker?) {
        marker?.icon = ContextCompat.getDrawable(map.context, R.drawable.geo_fill_icon_red)
    }
    override fun onMarkerDragEnd(marker: Marker?) {
        marker?.icon = ContextCompat.getDrawable(map.context, R.drawable.geo_fill_icon_185595)
    }
}