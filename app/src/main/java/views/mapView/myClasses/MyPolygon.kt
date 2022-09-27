package views.mapView.myClasses

import android.graphics.Bitmap
import models.mapModel.apiEntities.PictureApiResponse
import org.osmdroid.views.overlay.Polygon

class MyPolygon(val id: Int) : Polygon() {
    var image : Bitmap? = null
    var pictureApiResponse : PictureApiResponse? = null
}