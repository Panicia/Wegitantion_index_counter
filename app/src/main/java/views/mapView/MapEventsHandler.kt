package views.mapView

import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.util.GeoPoint
import views.mapView.buttons.MarkerAddAvailableBtn

class MapEventsHandler(
    private val button : MarkerAddAvailableBtn,
    private val mapOverlayHandler : MapOverlayHandler

    ) : MapEventsReceiver {

    override fun singleTapConfirmedHelper(p: GeoPoint?): Boolean {
        if (button.isEnabled) {
            if(p != null)
                mapOverlayHandler.placeMarker(p)
        }
        return true
    }
    override fun longPressHelper(p: GeoPoint?): Boolean {
        return false
    }
}