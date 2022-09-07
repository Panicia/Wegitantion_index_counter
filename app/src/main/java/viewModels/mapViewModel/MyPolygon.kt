package viewModels.mapViewModel

import android.graphics.Bitmap
import org.osmdroid.views.overlay.Polygon

class MyPolygon(val index : Long) : Polygon() {
    var image : Bitmap? = null
}