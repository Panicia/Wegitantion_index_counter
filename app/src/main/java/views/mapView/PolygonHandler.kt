package views.mapView

import android.graphics.Color
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polygon

class PolygonHandler(
    private val map : MapView) {

    private var polygon = Polygon()
    private lateinit var geoPointsArray : ArrayList<GeoPoint>

    fun createNewPolygon(markersArray: ArrayList<Marker>) {
        if(markersArray.size > 0) {
            geoPointsArray = makeGeoPointsArray(markersArray)
            createPolygonFromPoints(geoPointsArray)
        }
    }

    private fun makeGeoPointsArray(markersArray : ArrayList<Marker>) : ArrayList<GeoPoint> {
        val array = ArrayList<GeoPoint>()
        for(i in 0 until markersArray.size) {
            val geoPoint = GeoPoint(markersArray[i].position)
            array.add(geoPoint)
        }
        array.add(array[0])
        return array
    }

    fun redrawPolygonIfNeeded(markersArray: ArrayList<Marker>) {
        if(markersArray.size > 2) {
            deletePolygon()
            geoPointsArray = makeGeoPointsArray(markersArray)
            createPolygonFromPoints(geoPointsArray)
        }
        else
            deletePolygon()
    }

    private fun createPolygonFromPoints(geoPointsArray : ArrayList<GeoPoint>) {
        polygon.fillPaint.color = Color.parseColor("#1EFFE70E")
        polygon.points = geoPointsArray
        polygon.title = "Polygon 1"
        map.overlays.add(0, polygon)
    }

    fun deletePolygon() {
        if(map.overlays.contains(polygon)) {
            map.overlays.remove(polygon)
        }
    }
}