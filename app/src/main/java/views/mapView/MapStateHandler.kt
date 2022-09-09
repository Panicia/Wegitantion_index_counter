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

    fun convertAndSaveMap(map : MapView) {
        convertMapToState(map)
        mapViewModel.saveState()
    }

    fun loadMap(map : MapView, markersAdder: MapEventsHandler) {
        if(mapViewModel.stateExist) {
            restoreMapFromState(map)
            setMapDefaultsBasis(map, markersAdder)
        }
        else {
            loadDefaultMapState(map, markersAdder)
        }
    }

    private fun restoreMapFromState(map : MapView) {
        for(polygon in mapViewModel.mapStateLive.value!!.myPolygons) {
            map.overlays.add(polygon)
        }
        map.controller.setZoom(mapViewModel.mapStateLive.value!!.mapZoom)
        map.controller.setCenter(mapViewModel.mapStateLive.value!!.mapCenter)
    }

    private fun loadDefaultMapState(map : MapView, markersAdder: MapEventsHandler) {
        setMapDefaults(map)
        setMapDefaultsBasis(map, markersAdder)
    }

    private fun convertMapToState(map: MapView) {
        val mapCenter = GeoPoint(map.mapCenter.latitude, map.mapCenter.longitude)
        val mapZoom = map.zoomLevelDouble
        val mapPolygons = arrayListOf<MyPolygon>()
        for(i in 0 until map.overlays.count()) {
            if(map.overlays[i] is Polygon) {
                val overlayPolygon = map.overlays[i] as Polygon
                val myPolygon = MyPolygon(mapViewModel.getLastPolygonId())
                myPolygon.points = overlayPolygon.actualPoints
                myPolygon.title = overlayPolygon.title
                mapPolygons.add(myPolygon)
            }
        }
        mapViewModel.mapStateLive.value = MapState(mapCenter, mapZoom, mapPolygons)
    }

    private fun setMapDefaults(map: MapView) {
        map.controller.setZoom(mapViewModel.defaultState.mapZoom)
        map.controller.setCenter(mapViewModel.defaultState.mapCenter)
    }

    private fun setMapDefaultsBasis(map : MapView, markersAdder : MapEventsHandler) {
        val mapEventsOverlay = MapEventsOverlay(markersAdder)
        map.overlays.add(mapEventsOverlay)
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