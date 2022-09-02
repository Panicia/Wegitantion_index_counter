package views.mapView

import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay
import org.osmdroid.views.overlay.infowindow.InfoWindow

class MapOverlayHandler(
    private val map : MapView,
    )  {

    private val rotationGestureOverlay = RotationGestureOverlay(map)
    private val polygonHandler = PolygonHandler(map)
    private val markersHandler = MarkersHandler(map, polygonHandler)

    init {
        map.overlays.add(rotationGestureOverlay)
        rotationGestureOverlay.isEnabled = false
    }

    fun rotateOn() {
        rotationGestureOverlay.isEnabled = true
    }

    fun rotateOff() {
        rotationGestureOverlay.isEnabled = false
    }

    fun placeMarker(p: GeoPoint) {
        markersHandler.createMarker(p)
    }

    fun deleteAll() {
        markersHandler.deleteAllMarkers()
        polygonHandler.deletePolygon()
        InfoWindow.closeAllInfoWindowsOn(map)
        map.invalidate()
    }
}
