package views.mapView.myClasses

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import models.mapModel.apiEntities.PictureApiResponse
import views.mapView.mapOverlays.MapOverlayHandler
import java.lang.Exception

class PictureTarget(
    private val mapOverlayHandler: MapOverlayHandler,
    private val pictureApiResponse: PictureApiResponse

    ) : Target {

    override fun onPrepareLoad(placeHolderDrawable: Drawable?) {

    }

    override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
        if(bitmap != null) {
            mapOverlayHandler.placeBitmap(pictureApiResponse, bitmap)
        }
    }

    override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {

    }
}