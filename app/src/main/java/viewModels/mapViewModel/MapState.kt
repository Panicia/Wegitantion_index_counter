package viewModels.mapViewModel

import org.osmdroid.util.GeoPoint

data class MapState(
    var mapCenter : GeoPoint,
    var mapZoom : Double,
    var myPolygons : ArrayList<MyPolygon>
)
