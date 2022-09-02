package views.mapView

import com.example.wegitantionindexcounter.MarkerAddAvailableBtn
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.util.GeoPoint

class MarkersAdder(
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