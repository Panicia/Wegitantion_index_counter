package viewModels.mapViewModel

import models.mapModel.MapRepository
import androidx.lifecycle.ViewModel
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polygon

class MapViewModel(
    private val mapRepository : MapRepository
) : ViewModel() {

    val defaultZoom = 10.0
    val startPointAhrangelsk = GeoPoint(64.54008896758883, 40.51580601698074)

    lateinit var markersArray : ArrayList<Marker>
    lateinit var polygonsArray : Polygon
    var mapZoom : Double = defaultZoom
    var mapCenter : GeoPoint = startPointAhrangelsk

    init {

    }

}