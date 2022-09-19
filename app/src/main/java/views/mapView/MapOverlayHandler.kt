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
    private val onMarkerDragListener = MyOnMarkerDragListener(markersManager, polygonsManager)

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

    fun placeMarker(p: GeoPoint) {
        markersManager.createMarker(p)
    }

    fun placePolygon(myPolygons: Array<MyPolygon>) {
        polygonsManager
    }

    fun deleteAll() {
        markersManager.deleteAllMarkers()
        polygonsManager.deleteAllPolygons()
        InfoWindow.closeAllInfoWindowsOn(map)
        map.invalidate()
    }
}
