package views.mapView.mapOverlays

import android.graphics.Bitmap
import android.graphics.Color
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.wegitantionindexcounter.R
import models.mapModel.apiEntities.PictureApiResponse
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.infowindow.InfoWindow
import viewModels.mapViewModel.MapViewModel
import views.mapView.myClasses.MyPolygon

class PolygonsManager(
    private val map : MapView,
    private val markersManager: MarkersManager,
    private val mapViewModel: MapViewModel,
    private val bitmapManager: BitmapManager
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

    private fun getLocalPolygonId(): Int {
        var polygonNumber = 0
        for(overlay in map.overlays) {
            if(overlay is MyPolygon) {
                polygonNumber++
            }
        }
        return polygonNumber
    }

    fun setPictureToPolygon(pictureApiResponse: PictureApiResponse, bitmap: Bitmap) {
        for(polygon in polygons) {
            if(polygon.id == pictureApiResponse.id) {
                polygon.image = bitmap
                polygon.pictureApiResponse = pictureApiResponse
                break
            }
        }
    }

    fun addNewPolygonAndStartEdit() {
        val newPolygon = MyPolygon(getLocalPolygonId())
        addExistingPolygon(newPolygon)
        startEditPolygon(newPolygon)
    }

    fun addExistingPolygon(polygon: MyPolygon) {
        polygon.fillPaint.color = Color.parseColor("#1EFFE70E")
        if(polygon.title == null)
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
            val closeButton = mView.findViewById<Button>(R.id.closeButton)
            val editText = mView.findViewById<EditText>(R.id.editText)
            editText.setText(polygon.title)
            var loadNDVAButtonEnabled = false

            editText.setOnFocusChangeListener { _, _ ->
                polygon.title = editText.text.toString()
            }

            closeButton.setOnClickListener {
                close()
            }

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
                if(!loadNDVAButtonEnabled) {
                    if (polygon.image == null) {
                        mapViewModel.getPolygonPicture(polygon)
                    } else {
                        bitmapManager.placeBitmap(polygon.pictureApiResponse!!, polygon.image!!)
                    }
                    loadNDVAButtonEnabled = true
                }
                else {
                    bitmapManager.hideBitmap(polygon)
                    loadNDVAButtonEnabled = false
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
