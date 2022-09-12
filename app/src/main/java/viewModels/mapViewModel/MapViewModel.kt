package viewModels.mapViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
        viewModelScope.launch {
            mapRepository.saveState(state, mapRepository.defaultStateId)
        }
    }

    private fun updateState() {
        var newState = mapStateLive.value!!
        viewModelScope.launch{
            var stateExist: Boolean
            withContext(Dispatchers.IO) {
                stateExist = mapRepository.checkStateSavedIsExist()

                if (stateExist) {
                    newState = mapRepository.loadState(mapRepository.defaultStateId)
                }
            }
            withContext(Dispatchers.Main) {
                mapStateLive.value = newState
            }
        }
    }
}