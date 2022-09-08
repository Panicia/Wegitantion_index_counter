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

    var stateExist = false
    var mapState = MapState(mapRepository.startPointAhrangelsk, mapRepository.defaultZoom, ArrayList())

    init {
        updateState()
    }

    fun saveState() {
        viewModelScope.launch {
            mapRepository.saveState(mapState, mapRepository.defaultStateId)
        }
        stateExist = true
    }

    fun getLastPolygonId() : Long {
        var lastId = 0L
        viewModelScope.launch {
            lastId = mapRepository.getNextPolygonId()
        }
        return lastId
    }

    private fun updateState() {
        viewModelScope.launch {
            stateExist = mapRepository.checkStateSavedIsExist()
            if(stateExist) {
                mapState = mapRepository.loadState(mapRepository.defaultStateId)
            }
        }
    }
}