package views.mapView

import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay
import org.osmdroid.views.overlay.infowindow.InfoWindow
import viewModels.mapViewModel.MyPolygon

class MapOverlayHandler(
    private val map : MapView,
    )  {

    private val rotationGestureOverlay = RotationGestureOverlay(map)
    private val markersManager = MarkersManager(map)
    private val polygonsManager = PolygonsManager(map)
    private val onMarkerDragListener = MyOnMarkerDragListener(map, markersManager, polygonsManager)

    init {
        markersManager.setMyOnMarkerDragListener(onMarkerDragListener)
        map.overlays.add(rotationGestureOverlay)
        rotationGestureOverlay.isEnabled = false
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

    fun addMarkerToActivePolygon(point: GeoPoint) {
        if(isPolygonEditing()) {
            val polygon = polygonsManager.addPointToActivePolygon(point)
            markersManager.redrawMarkersOfPolygon(polygon)
        }
    }

    fun placeExistingPolygons(myPolygons: Array<MyPolygon>) {
        for(polygon in myPolygons) {
            polygonsManager.addExistingPolygon(polygon)
        }
    }

    fun deleteAll() {
        markersManager.deleteAllMarkers()
        polygonsManager.deleteAllPolygons()
        InfoWindow.closeAllInfoWindowsOn(map)
        map.invalidate()
    }
}
