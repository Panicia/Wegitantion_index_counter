package viewModels.mapViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MapViewModel(
    private val mapRepository : MapRepository
) : ViewModel() {

    private var mapStateLive = MutableLiveData<MapState>()
    private val defaultState = mapRepository.getDefaultState()

    init {
        mapStateLive.value = defaultState
    }

    fun getMapStateLive() : LiveData<MapState> {
        return mapStateLive
    }

    fun saveState(state: MapState) {
        viewModelScope.launch(Dispatchers.IO) {
            mapRepository.saveState(state, mapRepository.defaultStateId)
        }
    }

    fun updateState() {
        viewModelScope.launch(Dispatchers.IO) {
            if (mapRepository.checkStateSavedIsExist()) {
                mapStateLive.postValue(mapRepository.loadState(mapRepository.defaultStateId))
            }
        }
    }
}