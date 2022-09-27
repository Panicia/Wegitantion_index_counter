package views.mapView.mapOverlays

import android.graphics.Bitmap
import models.mapModel.apiEntities.PictureApiResponse
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import views.mapView.myClasses.MyGroundOverlay
import views.mapView.myClasses.MyPolygon

class BitmapManager(
    private val map: MapView
) {
    fun placeBitmap(pictureApiResponse: PictureApiResponse, bitmap: Bitmap) {
        val myGroundOverlay = MyGroundOverlay(pictureApiResponse.id)
        val mapCenter1 = GeoPoint(pictureApiResponse.point1[0], pictureApiResponse.point1[1])
        val mapCenter2 = GeoPoint(pictureApiResponse.point2[0], pictureApiResponse.point2[1])
        myGroundOverlay.setPosition(mapCenter1, mapCenter2)
        myGroundOverlay.image = bitmap
        myGroundOverlay.transparency = 0.25f
        myGroundOverlay.bearing = 0f
        map.overlays.add(myGroundOverlay)
        map.invalidate()
    }
    fun hideBitmap(polygon: MyPolygon) {
        for(overlay in map.overlays) {
            if(overlay is MyGroundOverlay) {
                if(overlay.polygonId == polygon.id) {
                    map.overlays.remove(overlay)
                }
            }
        }
    }
}