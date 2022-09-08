package views.mapView

import android.util.DisplayMetrics
import android.view.View
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.*
import viewModels.mapViewModel.MapState
import viewModels.mapViewModel.MapViewModel
import viewModels.mapViewModel.MyPolygon

class MapStateHandler(
    private val mapViewModel : MapViewModel
) {
    private lateinit var mapOverlays : List<Overlay>
    private lateinit var mapEventsOverlay : MapEventsOverlay

    private var mapWasSaved = false

    init {

    }

    fun saveMapToMapState(map : MapView) {
        val mapCenter = GeoPoint(map.mapCenter.latitude, map.mapCenter.longitude)
        val mapZoom = map.zoomLevelDouble
        val mapPolygons = arrayListOf<MyPolygon>()
        for(i in map.overlays) {
            if(i is Polygon) {

                val myPolygon = MyPolygon()
            }
        }
        mapViewModel.mapState = MapState(mapCenter, mapZoom, mapPolygons)
        mapWasSaved = true
    }

    fun restoreMapFromMapState(map : MapView) {

    }

    fun defaultMapState(map : MapView, markersAdder: MapEventsHandler) {
        setMapDefaults(map, markersAdder)
        setMapDefaultsBasis(map)
    }

    private fun setMapSaved(map : MapView) {
        setMapDefaultsBasis(map)
    }

    private fun setMapOverlays(map : MapView) {

    }

    private fun setMapDefaults(map : MapView, markersAdder : MapEventsHandler) {
        mapEventsOverlay = MapEventsOverlay(markersAdder)
        map.overlays.add(mapEventsOverlay)
        map.controller.setZoom(mapViewModel.mapState.mapZoom)
        map.controller.setCenter(mapViewModel.mapState.mapCenter)
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