package viewModels.mapViewModel

import org.osmdroid.util.GeoPoint
import views.mapView.myClasses.MyPolygon

data class MapState(
    var mapCenter : GeoPoint,
    var mapZoom : Double,
    var myPolygons : ArrayList<MyPolygon>
)
