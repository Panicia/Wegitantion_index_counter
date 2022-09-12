package viewModels.mapViewModel

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import models.mapModel.MapDao
import models.mapModel.entities.MarkerRepos
import models.mapModel.entities.PolygonRepos
import models.mapModel.entities.StateRepos
import org.osmdroid.util.GeoPoint

class MapRepository(
    private val mapDao: MapDao
) {

    val defaultStateId = 0L

    private val defaultZoom = 10.0
    private val startPointAhrangelsk = GeoPoint(64.54008896758883, 40.51580601698074)

    fun getDefaultState(): MapState {
        return MapState(startPointAhrangelsk, defaultZoom, ArrayList())
    }

    fun checkStateSavedIsExist(): Boolean {
        val states = mapDao.getAllStates()
        return states.isNotEmpty()
    }

    fun saveState(mapState: MapState, stateId: Long) {
        //withContext(Dispatchers.IO) {
            deleteStateFromDatabase(stateId)
            saveStateToDataBase(mapState, stateId)
        //}
    }

    fun loadState(stateId: Long) : MapState {
        return loadStateFromDataBase(stateId)
    }

    fun deleteAll() {
        deleteAllFromDatabase()
    }

    private fun getNextPolygonIdFromDatabase(): Long {
        val polygons = mapDao.getAllPolygons()
        return if(polygons.isNotEmpty()) {
            polygons.count().toLong()
        } else
            0L
    }

    private fun saveStateToDataBase(mapState: MapState, stateId: Long) {
        val newState = StateRepos(stateId, mapState.mapZoom, mapState.mapCenter.latitude, mapState.mapCenter.longitude)
        mapDao.insertState(newState)
        if(mapState.myPolygons.isNotEmpty()) {
            for (myPolygon in mapState.myPolygons) {
                val nextPolygonIndex = getNextPolygonIdFromDatabase()
                val newPolygon = PolygonRepos(nextPolygonIndex, myPolygon.title, null, stateId)
                mapDao.insertPolygon(newPolygon)
                if(myPolygon.actualPoints.isNotEmpty()) {
                    for (point in myPolygon.actualPoints) {
                        val newMarker = MarkerRepos(0, point.latitude, point.longitude, nextPolygonIndex)
                        mapDao.insertMarker(newMarker)
                    }
                }
            }
        }
    }

    private fun loadStateFromDataBase(stateId: Long): MapState {
        val state = mapDao.getState(stateId)
        var finalZoom = defaultZoom
        var finalCenter = startPointAhrangelsk
        val finalMyPolygons = ArrayList<MyPolygon>()
        if(state != null) {
            finalCenter = GeoPoint(state.centerLat, state.centerLon)
            finalZoom = state.zoom
        }
        val polygons = mapDao.getPolygonsFromState(stateId)
        if(polygons.isNotEmpty()) {
            for (polygon in polygons) {
                val myPolygon = MyPolygon()
                val markersFromPoly = mapDao.getMarkersFromPoly(polygon.id)
                if(markersFromPoly.isNotEmpty()) {
                    for (marker in markersFromPoly) {
                        val geoPoint = GeoPoint(marker.lat, marker.lon)
                        myPolygon.addPoint(geoPoint)
                    }
                    finalMyPolygons.add(myPolygon)
                }
            }
        }
        return MapState(finalCenter, finalZoom, finalMyPolygons)
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