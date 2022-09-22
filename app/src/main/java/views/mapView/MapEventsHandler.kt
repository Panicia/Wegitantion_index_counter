package views.mapView

import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.util.GeoPoint
import views.mapView.buttons.MarkerAddAvailableBtn
import views.mapView.mapOverlays.MapOverlayHandler

class MapEventsHandler(
    private val button : MarkerAddAvailableBtn,
    private val mapOverlayHandler : MapOverlayHandler

    ) : MapEventsReceiver {

    override fun singleTapConfirmedHelper(p: GeoPoint?): Boolean {
        if (button.isEnabled) {
            if(p != null)
                if(mapOverlayHandler.isPolygonEditing()) {
                    mapOverlayHandler.addMarkerToActivePolygon(p)
                }
                else {
                    mapOverlayHandler.addNewActivePolygon()
                    mapOverlayHandler.addMarkerToActivePolygon(p)
                }
        }
        return false
    }
    override fun longPressHelper(p: GeoPoint?): Boolean {
        return false
    }
}