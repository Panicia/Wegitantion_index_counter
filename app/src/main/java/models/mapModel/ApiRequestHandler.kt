package models.mapModel

import models.mapModel.apiEntities.PictureApiRequest
import models.mapModel.apiEntities.PictureApiResponse
import viewModels.mapViewModel.MyPolygon

class ApiRequestHandler(private val mapApi: MapApi) {

    suspend fun sendRequest(polygon: MyPolygon): PictureApiResponse {
        val array = Array(polygon.actualPoints.size) {
            arrayOf(polygon.actualPoints[it].latitude, polygon.actualPoints[it].longitude)
        }
        val pictureApiRequest = PictureApiRequest(polygon.title, array)
        return mapApi.getPicture(pictureApiRequest)
    }

}