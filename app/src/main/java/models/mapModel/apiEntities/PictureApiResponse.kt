package models.mapModel.apiEntities


data class PictureApiResponse(
    val id: Int,
    val name: String,
    val url: String,
    val point1: Array<Double>,
    val point2: Array<Double>
)