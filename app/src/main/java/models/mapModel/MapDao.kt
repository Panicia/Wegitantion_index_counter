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
    fun deleteState(state: StateRepos)

    @Query("SELECT * FROM states WHERE id = :stateId")
    fun getState(stateId : Long): StateRepos

    @Update
    fun updateState(vararg state: StateRepos)


    // Polygon functions
    @Insert
    fun insertPolygon(polygon: PolygonRepos)

    @Insert
    fun insertPolygons(polygon: List<PolygonRepos>)

    @Delete
    fun deletePolygon(polygon: PolygonRepos)

    @Query("SELECT * FROM polygons WHERE stateId = :stateId")
    fun getPolygonsFromState(stateId : Long): List<PolygonRepos>

    @Query("SELECT * FROM polygons WHERE id = :polId")
    fun getPolygon(polId: Long): PolygonRepos

    @Update
    fun updatePolygon(vararg polygon: PolygonRepos)


    // Marker functions
    @Insert
    fun insertMarker(marker: MarkerRepos)

    @Insert
    fun insertMarkers(markers: List<MarkerRepos>)

    @Delete
    fun deleteMarker(marker: MarkerRepos)

    @Query("SELECT * FROM markers " +
            "INNER JOIN polygons ON polygons.id = markers.polId " +
            "INNER JOIN states ON states.id = polygons.stateId " +
            "WHERE states.id = :stateId")
    fun getMarkersFromState(stateId: Long): List<MarkerRepos>

    @Query("SELECT * FROM markers WHERE polId = :polId")
    fun getMarkersFromPoly(polId: Long): List<MarkerRepos>

    @Query("SELECT * FROM markers WHERE id = :markerId")
    fun getMarker(markerId: Long): MarkerRepos

    @Update
    fun updateMarker(vararg marker: MarkerRepos)
}