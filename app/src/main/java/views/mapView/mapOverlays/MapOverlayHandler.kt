package views.mapView.mapOverlays

import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay
import org.osmdroid.views.overlay.infowindow.InfoWindow
import viewModels.mapViewModel.MapViewModel
import viewModels.mapViewModel.MyPolygon

class MapOverlayHandler(
    private val map : MapView,
    private val mapViewModel: MapViewModel

    )  {

    private val rotationGestureOverlay = RotationGestureOverlay(map)
    private val markersManager = MarkersManager(map, this)
    private val polygonsManager = PolygonsManager(map, markersManager, mapViewModel)
    private val onMarkerDragListener = MyOnMarkerDragListener(map, this)

    init {
        map.overlays.add(rotationGestureOverlay)
        rotationGestureOverlay.isEnabled = false
    }

    fun getOnMarkerDrugListener() : MyOnMarkerDragListener {
        return onMarkerDragListener
    }

    fun isPolygonEditing() : Boolean {
        return polygonsManager.isPolygonEditing()
    }

    fun rotateOn() {
        rotationGestureOverlay.isEnabled = true
    }

    fun rotateOff() {
        rotationGestureOverlay.isEnabled = false
    }

    fun addNewActivePolygon() {
        polygonsManager.addNewPolygonAndStartEdit()
    }

    fun redrawActivePolygon() {
        if(isPolygonEditing())
            polygonsManager.redrawActivePolygonFromMarkers()
    }

    fun addMarkerToActivePolygon(point: GeoPoint) {
        if(isPolygonEditing()) {
            val polygon = polygonsManager.addPointToActivePolygon(point)
            markersManager.redrawMarkersOfPolygon(polygon)
        }
    }

    fun placeExistingPolygon(polygon: MyPolygon) {
        polygonsManager.addExistingPolygon(polygon)
    }

    fun deleteAll() {
        markersManager.hideMarkersOfActivePolygon()
        polygonsManager.deleteAllPolygons()
        InfoWindow.closeAllInfoWindowsOn(map)
        map.invalidate()
    }
}
