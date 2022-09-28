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
import views.mapView.myClasses.MyPolygon
import views.mapView.mapOverlays.MapOverlayHandler


class MapStateHandler(
    private val mapViewModel : MapViewModel,
    private val mapOverlayHandler: MapOverlayHandler
    ) {

    private val defaultZoom = 10.0
    private val startPointAhrangelsk = GeoPoint(64.54008896758883, 40.51580601698074)

    init {
        mapViewModel.updateState()
    }

    fun convertAndSaveMap(map : MapView) {
        val state = convertMapToState(map)
        mapViewModel.saveState(state)
    }

    fun loadMap(map : MapView, markersAdder: MapEventsHandler) {
        restoreMapFromState(map)
        setMapDefaultsBasis(map, markersAdder)
    }

    fun setHome(map: MapView) {
        map.controller.setCenter(startPointAhrangelsk)
        map.controller.setZoom(defaultZoom)
        map.mapOrientation = 0f
    }

    private fun restoreMapFromState(map : MapView) {
        if(mapViewModel.getMapStateLive().value!!.myPolygons.isNotEmpty()) {
            for (polygon in mapViewModel.getMapStateLive().value!!.myPolygons) {
                mapOverlayHandler.placeExistingPolygon(polygon)
            }
        }
        map.controller.setZoom(mapViewModel.getMapStateLive().value!!.mapZoom)
        map.controller.setCenter(mapViewModel.getMapStateLive().value!!.mapCenter)
    }

    private fun convertMapToState(map: MapView): MapState {
        val mapCenter = GeoPoint(map.mapCenter.latitude, map.mapCenter.longitude)
        val mapZoom = map.zoomLevelDouble
        val mapPolygons = ArrayList<MyPolygon>()
        for(i in 0 until map.overlays.count()) {
            if(map.overlays[i] is MyPolygon) {
                mapPolygons.add(map.overlays[i] as MyPolygon)
            }
        }
        return MapState(mapCenter, mapZoom, mapPolygons)
    }

    private fun setMapDefaultsBasis(map : MapView, markersAdder : MapEventsHandler) {
        val mapEventsOverlay = MapEventsOverlay(markersAdder)
        map.overlays.add(mapEventsOverlay)
        map.setLayerType(View.LAYER_TYPE_HARDWARE, null)
        map.setTileSource(TileSourceFactory.MAPNIK)
        map.setMultiTouchControls(true)
        map.zoomController.setVisibility(CustomZoomButtonsController.Visibility.NEVER)
        val dm : DisplayMetrics = map.context.resources.displayMetrics
        val scaleBarOverlay = ScaleBarOverlay(map)
        scaleBarOverlay.setCentred(true)
        scaleBarOverlay.setScaleBarOffset(dm.widthPixels / 2, 10)
        map.overlays.add(scaleBarOverlay)
    }

    private fun deleteMapOverlays(map: MapView) {
        map.overlays.clear()
    }
}