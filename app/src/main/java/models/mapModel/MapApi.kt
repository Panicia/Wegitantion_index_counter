package models.mapModel
import models.mapModel.apiEntities.PictureApiRequest
import models.mapModel.apiEntities.PictureApiResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface MapApi {

    @POST("send_picture")
    suspend fun getPicture(@Body request: PictureApiRequest): PictureApiResponse

}