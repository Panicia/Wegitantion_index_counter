package models.mapModel

import androidx.room.*
import models.mapModel.entities.Marker
import models.mapModel.entities.Polygon
import models.mapModel.entities.State

@Dao
interface MapDao {

    // State functions
    @Insert
    fun insertState(state: State)

    @Delete
    fun deleteState(state: State)

    @Query("SELECT * FROM states WHERE id = :stateId")
    fun getState(stateId : Long): State

    @Update
    fun updateState(vararg state: State)


    // Polygon functions
    @Insert
    fun insertPolygon(polygon: Polygon)

    @Insert
    fun insertPolygons(polygon: List<Polygon>)

    @Delete
    fun deletePolygon(polygon: Polygon)

    @Query("SELECT * FROM polygons WHERE stateId = :stateId")
    fun getPolygonsFromState(stateId : Long): List<Polygon>

    @Query("SELECT * FROM polygons WHERE id = :polId")
    fun getPolygon(polId: Long): Polygon

    @Update
    fun updatePolygon(vararg polygon: Polygon)


    // Marker functions
    @Insert
    fun insertMarker(marker: Marker)

    @Insert
    fun insertMarkers(markers: List<Marker>)

    @Delete
    fun deleteMarker(marker: Marker)

    @Query("SELECT * FROM markers " +
            "INNER JOIN polygons ON polygons.id = markers.polId " +
            "INNER JOIN states ON states.id = polygons.stateId " +
            "WHERE states.id = :stateId")
    fun getMarkersFromState(stateId: Long): List<Marker>

    @Query("SELECT * FROM markers WHERE polId = :polId")
    fun getMarkersFromPoly(polId: Long): List<Marker>

    @Query("SELECT * FROM markers WHERE id = :markerId")
    fun getMarker(markerId: Long): Marker

    @Update
    fun updateMarker(vararg marker: Marker)
}