package views.mapView

import android.graphics.Color
import android.widget.Button
import android.widget.TextView
import androidx.core.view.contains
import com.example.wegitantionindexcounter.R
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polygon
import org.osmdroid.views.overlay.infowindow.InfoWindow
import viewModels.mapViewModel.MyPolygon

class PolygonsManager(
    private val map : MapView
    ) {

    private val polygons = ArrayList<MyPolygon>()
    var activePolygon : MyPolygon? = null

    fun isPolygonEditing() : Boolean {
        return activePolygon != null
    }

    fun startEditPolygon(polygon: MyPolygon) {
        activePolygon = polygon
    }

    fun stopEditPolygon() {
        if(activePolygon != null) {
            if(activePolygon!!.actualPoints != null) {
                if(activePolygon!!.actualPoints.count() < 3) {
                    deleteActivePolygon()
                }
            }
        }
        else
            activePolygon = null
    }

    fun addNewPolygonAndStartEdit() {
        val newPolygon = MyPolygon()
        addExistingPolygon(newPolygon)
        startEditPolygon(newPolygon)
    }

    fun addExistingPolygon(polygon: MyPolygon) {
        polygon.fillPaint.color = Color.parseColor("#1EFFE70E")
        polygon.title = "Polygon ${polygons.count()}"
        polygon.infoWindow = PolygonInfoWindow(map, polygon)
        polygons.add(polygon)
        map.overlays.add(polygon)
    }

    fun redrawActivePolygon(): MyPolygon {
        //activePolygon
        return MyPolygon()
    }

    fun deleteAllPolygonsFromOverlay() {
        for(overlay in map.overlays) {
            if(overlay is MyPolygon) {
                map.overlays.remove(overlay)
            }
        }
    }

    fun addPointToActivePolygon(point: GeoPoint): MyPolygon {
        activePolygon?.addPoint(point)
        return activePolygon!!
    }

    fun deleteActivePolygon() {
        if(activePolygon != null) {
            deletePolygon(activePolygon!!)
        }
    }

    fun deletePolygon(polygon: MyPolygon) {
        if(map.overlays.contains(polygon)) {
            map.overlays.remove(polygon)
        }
        if(polygons.contains(polygon)) {
            polygons.remove(polygon)
        }
    }

    fun deleteAllPolygons() {
        stopEditPolygon()
        polygons.clear()
        activePolygon = null
    }

    inner class PolygonInfoWindow(
        private val map: MapView,
        private val polygon: MyPolygon,

        ) : InfoWindow(R.layout.polygon_layout, map) {

        override fun onOpen(item: Any?) {
            closeAllInfoWindowsOn(map)
            val deleteButton = mView.findViewById<Button>(R.id.delete_button)
            val textView = mView.findViewById<TextView>(R.id.text_view)
            textView.text = getPolygonPos()

            deleteButton.setOnClickListener {
                deletePolygon(polygon)
                closeAllInfoWindowsOn(map)
            }
            mView.setOnClickListener {
                close()
            }
        }
        private fun getPolygonPos(): String {
            return "lat: ${polygon.actualPoints[0].latitude}\nlon: ${polygon.actualPoints[0].longitude}"
        }
        override fun onClose() {

        }
    }
}