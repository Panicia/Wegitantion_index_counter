package models.mapModel

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import views.mapView.MapStateManager

class MapRepository(
    private val mapDao: MapDao) {

    suspend fun saveState(mapState: MapStateManager) {
        withContext(Dispatchers.IO) {


        }
    }

    suspend fun loadState() {
        withContext(Dispatchers.IO) {


        }
    }

    suspend fun deleteAll() {
        withContext(Dispatchers.IO) {


        }
    }
}