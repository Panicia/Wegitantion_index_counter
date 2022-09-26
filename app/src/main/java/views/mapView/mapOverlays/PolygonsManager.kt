package views.mapView.mapOverlays

import android.graphics.BitmapFactory
import android.graphics.Color
import android.widget.Button
import android.widget.TextView
import androidx.core.graphics.createBitmap
import com.example.wegitantionindexcounter.R
import com.squareup.picasso.Picasso
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.GroundOverlay
import org.osmdroid.views.overlay.infowindow.InfoWindow
import viewModels.mapViewModel.MapViewModel
import viewModels.mapViewModel.MyPolygon

class PolygonsManager(
    private val map : MapView,
    private val markersManager: MarkersManager,
    private val mapViewModel: MapViewModel

    ) {

    private val polygons = ArrayList<MyPolygon>()
    var activePolygon : MyPolygon? = null

    fun isPolygonEditing() : Boolean {
        return activePolygon != null
    }

    private fun startEditPolygon(polygon: MyPolygon) {
        activePolygon = polygon
    }

    fun stopEditPolygon() {
        if(activePolygon != null) {
            if(activePolygon!!.actualPoints != null) {
                if(activePolygon!!.actualPoints.count() < 3) {
                    deleteActivePolygon()
                }
            }
            activePolygon = null
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
        map.invalidate()
    }

    fun redrawActivePolygonFromMarkers() {
        activePolygon!!.points = markersManager.returnPoints()
    }

    private fun deleteAllPolygonsFromOverlay() {
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

    private fun deleteActivePolygon() {
        if(activePolygon != null) {
            deletePolygon(activePolygon!!)
        }
    }

    fun deletePolygon(polygon: MyPolygon) {
        markersManager.hideMarkersOfActivePolygon()
        if(map.overlays.contains(polygon)) {
            map.overlays.remove(polygon)
        }
        if(polygons.contains(polygon)) {
            polygons.remove(polygon)
        }
        if(polygon == activePolygon) {
            activePolygon = null
        }
    }

    fun deleteAllPolygons() {
        stopEditPolygon()
        polygons.clear()
        activePolygon = null
        deleteAllPolygonsFromOverlay()
    }

    inner class PolygonInfoWindow(
        private val map: MapView,
        private val polygon: MyPolygon,

        ) : InfoWindow(R.layout.polygon_layout, map) {

        override fun onOpen(item: Any?) {
            closeAllInfoWindowsOn(map)
            val deleteButton = mView.findViewById<Button>(R.id.delete_button)
            val editButton = mView.findViewById<Button>(R.id.edit_button)
            val loadNDVAButton = mView.findViewById<Button>(R.id.load_NDVI_button)
            val textView = mView.findViewById<TextView>(R.id.text_view)
            textView.text = polygon.title

            editButton.setOnClickListener {
                if(activePolygon != polygon) {
                    if (activePolygon != null) {
                        markersManager.hideMarkersOfActivePolygon()
                    }
                    markersManager.showMarkersOfPolygon(polygon)
                    activePolygon = polygon
                    startEditPolygon(polygon)
                }
                else {
                    activePolygon = null
                    markersManager.hideMarkersOfActivePolygon()
                    stopEditPolygon()
                }
            }

            loadNDVAButton.setOnClickListener {
                mapViewModel.getPolygonPicture(polygon)
                mapViewModel.getPictureResponseLive().observe(map.context) {
                    val myGroundOverlay = GroundOverlay()
                    val mapCenter1 = mapViewModel.getPictureResponseLive().value?.point1
                    val mapCenter2 = mapViewModel.getPictureResponseLive().value?.point2
                    myGroundOverlay.setPosition(mapCenter1, mapCenter2)
                    myGroundOverlay.image = d
                    myGroundOverlay.transparency = 0.25f
                    myGroundOverlay.bearing = 0f
                    map.overlays.add(myGroundOverlay)
                    map.invalidate()
                }
            }

            deleteButton.setOnClickListener {
                deletePolygon(polygon)
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
