package models.mapModel

import androidx.room.*
import models.mapModel.entities.MarkerRepos
import models.mapModel.entities.PolygonRepos
import models.mapModel.entities.StateRepos

@Dao
interface MapDao {

    // State functions
    @Insert
    fun insertState(state: StateRepos)

    @Delete
    fun deleteStates(vararg state: StateRepos)

    @Query("SELECT * FROM states WHERE id = :stateId")
    fun getState(stateId : Long): StateRepos?

    @Query("SELECT * FROM states")
    fun getAllStates() : List<StateRepos>

    @Update
    fun updateStates(vararg state: StateRepos)


    // Polygon functions
    @Insert
    fun insertPolygon(polygon: PolygonRepos)

    @Insert
    fun insertPolygons(polygon: List<PolygonRepos>)

    @Delete
    fun deletePolygons(vararg polygon: PolygonRepos)

    @Query("SELECT * FROM polygons WHERE stateId = :stateId")
    fun getPolygonsFromState(stateId : Long): List<PolygonRepos>

    @Query("SELECT * FROM polygons WHERE id = :polId")
    fun getPolygon(polId: Long): PolygonRepos?

    @Query("SELECT * FROM polygons")
    fun getAllPolygons() : List<PolygonRepos>

    @Update
    fun updatePolygons(vararg polygon: PolygonRepos)


    // Marker functions
    @Insert
    fun insertMarker(marker: MarkerRepos)

    @Insert
    fun insertMarkers(markers: List<MarkerRepos>)

    @Delete
    fun deleteMarkers(vararg marker: MarkerRepos)

    @Query("SELECT * FROM markers " +
            "INNER JOIN polygons ON polygons.id = markers.polId " +
            "INNER JOIN states ON states.id = polygons.stateId " +
            "WHERE states.id = :stateId")
    fun getMarkersFromState(stateId: Long): List<MarkerRepos>

    @Query("SELECT * FROM markers WHERE polId = :polId")
    fun getMarkersFromPoly(polId: Long): List<MarkerRepos>

    @Query("SELECT * FROM markers WHERE id = :markerId")
    fun getMarker(markerId: Long): MarkerRepos?

    @Query("SELECT * FROM markers")
    fun getAllMarkers() : List<MarkerRepos>

    @Update
    fun updateMarkers(vararg marker: MarkerRepos)
}