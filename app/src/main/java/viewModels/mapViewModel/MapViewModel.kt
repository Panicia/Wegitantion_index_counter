package viewModels.mapViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class MapViewModel(
    private val mapRepository : MapRepository
) : ViewModel() {

    var stateExist = false
    val defaultState = mapRepository.getDefaultState()
    var mapStateLive : MutableLiveData<MapState>

    init {
        mapStateLive = MutableLiveData()
        mapStateLive.value = defaultState
        checkStateExistence()
        updateState()
    }

    fun saveState() {
        viewModelScope.launch {
            mapRepository.saveState(mapStateLive.value!!, mapRepository.defaultStateId)
        }
    }

    fun getLastPolygonId() : Long {
        var lastId = 0L
        viewModelScope.launch {
            lastId = mapRepository.getNextPolygonId()
        }
        return lastId
    }

    private fun checkStateExistence() {
        stateExist = mapRepository.checkStateSavedIsExist()
    }

    private fun updateState() {
        viewModelScope.launch {
            if(stateExist) {
                mapStateLive.value = mapRepository.loadState(mapRepository.defaultStateId)
            }
        }
    }
}