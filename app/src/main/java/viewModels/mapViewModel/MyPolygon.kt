package viewModels.mapViewModel

import android.graphics.Bitmap
import android.view.MotionEvent
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Polygon

class MyPolygon : Polygon() {
    var image : Bitmap? = null

    override fun onSingleTapConfirmed(pEvent: MotionEvent?, pMapView: MapView?): Boolean {
        showInfoWindow()
        return true
    }

    override fun onDoubleTap(e: MotionEvent?, mapView: MapView?): Boolean {
        showInfoWindow()
        return true
    }
}