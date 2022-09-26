package models.mapModel

import androidx.room.*
import models.mapModel.dbEntities.MarkerRepos
import models.mapModel.dbEntities.PolygonRepos
import models.mapModel.dbEntities.StateRepos

@Dao
interface MapDao {

    // State functions
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertState(vararg state: StateRepos)

    @Delete
    fun deleteStates(vararg state: StateRepos)

    @Query("SELECT * FROM states WHERE id = :stateId")
    fun getState(stateId : Long): StateRepos?

    @Query("SELECT * FROM states")
    fun getAllStates() : Array<StateRepos>

    @Update
    fun updateStates(vararg state: StateRepos)


    // Polygon functions
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPolygon(vararg polygon: PolygonRepos)

    @Delete
    fun deletePolygons(vararg polygon: PolygonRepos)

    @Query("SELECT * FROM polygons WHERE stateId = :stateId")
    fun getPolygonsFromState(stateId : Long): Array<PolygonRepos>

    @Query("SELECT * FROM polygons WHERE id = :polId")
    fun getPolygon(polId: Long): PolygonRepos?

    @Query("SELECT * FROM polygons")
    fun getAllPolygons() : Array<PolygonRepos>

    @Update
    fun updatePolygons(vararg polygon: PolygonRepos)


    // Marker functions
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMarker(vararg marker: MarkerRepos)

    @Delete
    fun deleteMarkers(vararg marker: MarkerRepos)

    @Query("SELECT * FROM markers " +
            "INNER JOIN polygons ON polygons.id = markers.polId " +
            "INNER JOIN states ON states.id = polygons.stateId " +
            "WHERE states.id = :stateId")
    fun getMarkersFromState(stateId: Long): Array<MarkerRepos>

    @Query("SELECT * FROM markers WHERE polId = :polId")
    fun getMarkersFromPoly(polId: Long): Array<MarkerRepos>

    @Query("SELECT * FROM markers WHERE id = :markerId")
    fun getMarker(markerId: Long): MarkerRepos?

    @Query("SELECT * FROM markers")
    fun getAllMarkers() : Array<MarkerRepos>

    @Update
    fun updateMarkers(vararg marker: MarkerRepos)
}