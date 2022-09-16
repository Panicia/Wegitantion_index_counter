package viewModels.mapViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import models.mapModel.MapRepository

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
        //viewModelScope.launch {
            //withContext(Dispatchers.IO) {
                mapRepository.saveState(state, mapRepository.defaultStateId)
            //}
        //}
    }

    fun updateState() {
        //viewModelScope.launch {
            //withContext(Dispatchers.IO) {
                if (mapRepository.checkStateSavedIsExist()) {
                    mapStateLive.value = mapRepository.loadState(mapRepository.defaultStateId)
                }
            //}
        //}
    }
}