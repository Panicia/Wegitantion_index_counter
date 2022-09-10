package viewModels.mapViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MapViewModel(
    private val mapRepository : MapRepository
) : ViewModel() {

    var mapStateLive = MutableLiveData<MapState>()
    private val defaultState = mapRepository.getDefaultState()

    init {
        mapStateLive.value = defaultState
        updateState()
    }

    fun saveState(state: MapState) {
        mapStateLive.value = state
        viewModelScope.launch(Dispatchers.IO) {
            mapRepository.saveState(mapStateLive.value!!, mapRepository.defaultStateId)
        }
    }

    private fun updateState() {
        viewModelScope.launch(Dispatchers.IO) {
            val stateExist = mapRepository.checkStateSavedIsExist()
            if(stateExist) {
                mapStateLive.value = mapRepository.loadState(mapRepository.defaultStateId)
            }
        }
    }
}