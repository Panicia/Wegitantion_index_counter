package models.mapModel.apiEntities

import org.osmdroid.util.GeoPoint

data class PictureApiResponse(
    val id: Int,
    val name: String,
    val url: String,
    val point1: GeoPoint,
    val point2: GeoPoint
)