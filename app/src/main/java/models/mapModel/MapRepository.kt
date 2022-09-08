package models.mapModel

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import models.mapModel.entities.MarkerRepos
import models.mapModel.entities.PolygonRepos
import models.mapModel.entities.StateRepos
import org.osmdroid.util.GeoPoint
import viewModels.mapViewModel.MapState
import viewModels.mapViewModel.MyPolygon

class MapRepository(
    private val mapDao: MapDao) {

    val defaultStateId = 0L

    val defaultZoom = 10.0
    val startPointAhrangelsk = GeoPoint(64.54008896758883, 40.51580601698074)

    lateinit var mapState: MapState

    suspend fun saveState(mapState: MapState, stateId: Long) {
        withContext(Dispatchers.IO) {
            deleteStateFromDatabase(stateId)
            saveStateToDataBase(mapState, stateId)
        }
    }

    suspend fun loadState(stateId: Long): MapState {
        withContext(Dispatchers.IO) {
            loadStateFromDataBase(stateId)
        }
        return mapState
    }

    suspend fun deleteAll() {
        withContext(Dispatchers.IO) {
            deleteAllFromDatabase()
        }
    }

    private fun saveStateToDataBase(mapState: MapState, stateId: Long) {
        val newState = StateRepos(stateId, mapState.mapZoom, mapState.mapCenter.latitude, mapState.mapCenter.longitude)
        mapDao.insertState(newState)
        if(mapState.myPolygons.isNotEmpty()) {
            for (myPolygon in mapState.myPolygons) {
                val newPolygonId = getNewPolygonId()
                val newPolygon = PolygonRepos(newPolygonId, myPolygon.title, null, stateId)
                mapDao.insertPolygon(newPolygon)
                if(myPolygon.actualPoints.isNotEmpty()) {
                    for (point in myPolygon.actualPoints) {
                        val newMarker =
                            MarkerRepos(null, point.latitude, point.longitude, newPolygonId)
                        mapDao.insertMarker(newMarker)
                    }
                }
            }
        }
    }

    private fun getNewPolygonId(): Long {
        val polygons = mapDao.getAllPolygons()
        if(polygons.isEmpty()) {
            return 0
        }
        else
            return polygons.count().toLong()
    }

    private fun loadStateFromDataBase(stateId: Long) {
        val state = mapDao.getState(stateId)
        if(state != null) {
            val center = GeoPoint(state.centerLat, state.centerLon)
            mapState = MapState(center, state.zoom, ArrayList())
        }
        else
            mapState = MapState(startPointAhrangelsk, defaultZoom, ArrayList())
        val polygons = mapDao.getPolygonsFromState(stateId)
        if(polygons.isNotEmpty()) {
            for (polygon in polygons) {
                val myPolygon = MyPolygon(polygon.id)
                val markersFromPoly = mapDao.getMarkersFromPoly(polygon.id)
                if(markersFromPoly.isNotEmpty()) {
                    for (marker in markersFromPoly) {
                        val geoPoint = GeoPoint(marker.lat, marker.lon)
                        myPolygon.addPoint(geoPoint)
                    }
                    mapState.myPolygons.add(myPolygon)
                }
            }
        }
    }

    private fun deleteStateFromDatabase(stateId: Long) {
        val state = mapDao.getState(stateId)
        if(state != null) {
            val polygons = mapDao.getPolygonsFromState(stateId)
            if(polygons.isNotEmpty()) {
                val markers = mapDao.getMarkersFromState(stateId)
                if(markers.isNotEmpty()) {
                    mapDao.deleteMarkers(*markers)
                }
                mapDao.deletePolygons(*polygons)
            }
            mapDao.deleteStates(state)
        }
    }

    private fun deleteAllFromDatabase() {
        val states = mapDao.getAllStates()
        if(states.isNotEmpty()) {
            val polygons = mapDao.getAllPolygons()
            if(polygons.isNotEmpty()) {
                val markers = mapDao.getAllMarkers()
                if(markers.isNotEmpty()) {
                    mapDao.deleteMarkers(*markers)
                }
                mapDao.deletePolygons(*polygons)
            }
            mapDao.deleteStates(*states)
        }
    }
}