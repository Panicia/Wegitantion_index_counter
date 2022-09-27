package models.mapModel.apiEntities

class PictureApiRequest(
    val id: Int,
    val name: String,
    val polygon: Array<Array<Double>>
)