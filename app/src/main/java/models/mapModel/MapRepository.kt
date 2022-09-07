package models.mapModel

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.osmdroid.util.GeoPoint
import viewModels.mapViewModel.MapState
import viewModels.mapViewModel.MyPolygon

class MapRepository(
    private val mapDao: MapDao) {

    private val defaultStateId = 0L

    val defaultZoom = 10.0
    val startPointAhrangelsk = GeoPoint(64.54008896758883, 40.51580601698074)

    lateinit var mapState: MapState

    suspend fun saveState(mapState: MapState) {
        withContext(Dispatchers.IO) {
            saveStateToDataBase(mapState)
        }
    }

    suspend fun loadState(): MapState {
        withContext(Dispatchers.IO) {
            loadStateFromDataBase()
        }
        return mapState
    }

    suspend fun deleteAll() {
        withContext(Dispatchers.IO) {
            val states = mapDao.getAllStates()
            if(states.isNotEmpty()) {
                val polygons = mapDao.getAllPolygons()
                if(polygons.isNotEmpty()) {
                    val markers = mapDao.getAllMarkers()
                    if(markers.isNotEmpty()) {
                        mapDao.deleteMarkers()
                    }
                }
            }
        }
    }

    private fun saveStateToDataBase(mapState: MapState) {

    }

    private fun loadStateFromDataBase() {
        val state = mapDao.getState(defaultStateId)
        if(state != null) {
            val center = GeoPoint(state.centerLat, state.centerLon)
            mapState = MapState(center, state.zoom, ArrayList())
        }
        else
            mapState = MapState(startPointAhrangelsk, defaultZoom, ArrayList())
        val polygons = mapDao.getPolygonsFromState(defaultStateId)
        if(polygons.isNotEmpty()) {
            for (i in 0 until polygons.count()) {
                val myPolygon = MyPolygon(polygons[i].id)
                val markersFromPoly = mapDao.getMarkersFromPoly(polygons[i].id)
                if(markersFromPoly.isNotEmpty()) {
                    for (j in 0 until markersFromPoly.count()) {
                        val geoPoint = GeoPoint(markersFromPoly[j].lat, markersFromPoly[j].lon)
                        myPolygon.addPoint(geoPoint)
                    }
                    mapState.myPolygons.add(myPolygon)
                }
            }
        }
    }
}