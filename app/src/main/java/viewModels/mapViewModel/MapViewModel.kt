package viewModels.mapViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import models.mapModel.ApiRequestHandler
import models.mapModel.MapRepository
import models.mapModel.apiEntities.PictureApiRequest
import models.mapModel.apiEntities.PictureApiResponse

class MapViewModel(
    private val mapRepository : MapRepository,
    private val apiRequestHandler: ApiRequestHandler
) : ViewModel() {

    private var mapStateLive = MutableLiveData<MapState>()
    private var pictureResponseLive = MutableLiveData<PictureApiResponse>()

    private val defaultState = mapRepository.getDefaultState()

    init {
        mapStateLive.value = defaultState
    }

    fun getMapStateLive() : LiveData<MapState> {
        return mapStateLive
    }

    fun getPictureResponseLive(): LiveData<PictureApiResponse> {
        return pictureResponseLive
    }

    fun getPolygonPicture(polygon: MyPolygon) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                pictureResponseLive.postValue(apiRequestHandler.sendRequest(polygon))
            }
        }
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
                    //mapStateLive.postValue(mapRepository.loadState(mapRepository.defaultStateId))
                }
            //}
        //}
    }
}