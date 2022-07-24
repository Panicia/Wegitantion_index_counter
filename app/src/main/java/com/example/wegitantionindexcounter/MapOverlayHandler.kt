package com.example.wegitantionindexcounter

import android.content.Context
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay
import org.osmdroid.views.overlay.infowindow.InfoWindow

class MapOverlayHandler(
    private val map : MapView,
    private val context: Context,
    private val dynamicAreasView: DynamicAreasView,
    )  {

    private val rotationGestureOverlay = RotationGestureOverlay(map)
    private val polygonHandler = PolygonHandler(map)
    private val markersHandler = MarkersHandler(map, context, polygonHandler)


    init {
        map.overlays.add(rotationGestureOverlay)
        rotationGestureOverlay.isEnabled = false
    }

    fun setRotate() {
        rotationGestureOverlay.isEnabled = true
    }
    fun deleteRotate() {
        rotationGestureOverlay.isEnabled = false
    }

    fun placeMarker(p: GeoPoint) {
        val marker = markersHandler.createMarker(p)
        dynamicAreasView.addMarkerInView(marker)
    }

    fun deleteAll() {
        markersHandler.deleteAllMarkers()
        polygonHandler.deletePolygon()
        InfoWindow.closeAllInfoWindowsOn(map)
        map.invalidate()
    }
}
