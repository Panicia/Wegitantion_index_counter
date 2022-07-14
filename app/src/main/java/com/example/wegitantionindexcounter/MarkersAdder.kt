package com.example.wegitantionindexcounter

import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.util.GeoPoint

class MarkersAdder(_button : MarkerAddAvailableBtn, _mapOverlayHandler : MapOverlayHandler) :
    MapEventsReceiver {
    private val button  = _button
    private val mapOverlayHandler = _mapOverlayHandler
    override fun singleTapConfirmedHelper(p: GeoPoint?): Boolean {
        if (button.isEnabled) {
            mapOverlayHandler.setMarker(p)
        }
        return true
    }
    override fun longPressHelper(p: GeoPoint?): Boolean {
        if (button.isEnabled) {

        }
        return false
    }
}