package views.mapView

import android.util.DisplayMetrics
import android.view.View
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.*
import viewModels.MapViewModel

class MapState(
    private val mapViewModel : MapViewModel) {

    private var defaultMapZoom = 10.0
    private var mapZoom = mapViewModel.mapZoom
    private var mapCenter = mapViewModel.mapCenter
    private lateinit var mapOverlays : List<Overlay>
    private lateinit var mapEventsOverlay : MapEventsOverlay

    private var mapWasSaved = false

    init {

    }

    fun saveMapState(map : MapView) {
        mapWasSaved = true

    }

    fun restoreMap(map : MapView) {

    }

    fun defaultMapState(map : MapView, markersAdder: MarkersAdder) {
        setMapDefaults(map, markersAdder)
        setMapDefaultsBasis(map)
    }

    private fun setMapSaved(map : MapView) {
        setMapDefaultsBasis(map)
    }

    private fun setMapOverlays(map : MapView) {

    }

    private fun setMapDefaults(map : MapView, markersAdder : MarkersAdder) {
        mapEventsOverlay = MapEventsOverlay(markersAdder)
        map.overlays.add(mapEventsOverlay)
        map.controller.setZoom(defaultMapZoom)
        map.controller.setCenter(mapViewModel.startPointAhrangelsk)
    }

    private fun setMapDefaultsBasis(map : MapView) {
        map.setLayerType(View.LAYER_TYPE_HARDWARE, null)
        map.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE)
        map.setMultiTouchControls(true)
        map.zoomController.setVisibility(CustomZoomButtonsController.Visibility.NEVER)
        val dm : DisplayMetrics = map.context.resources.displayMetrics
        val scaleBarOverlay = ScaleBarOverlay(map)
        scaleBarOverlay.setCentred(true)
        scaleBarOverlay.setScaleBarOffset(dm.widthPixels / 2, 10)
        map.overlays.add(scaleBarOverlay)
    }
}