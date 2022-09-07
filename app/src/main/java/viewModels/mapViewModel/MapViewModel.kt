package viewModels.mapViewModel

import models.mapModel.MapRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polygon

class MapViewModel(
    private val mapRepository : MapRepository
) : ViewModel() {

    var mapState = MapState(mapRepository.startPointAhrangelsk, mapRepository.defaultZoom, ArrayList())

    init {
        updateState()
    }

    private fun updateState() {
        viewModelScope.launch {
            mapState = mapRepository.loadState()
        }
    }
}