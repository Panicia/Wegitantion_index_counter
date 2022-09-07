package views.mapView.buttons

import android.content.res.ColorStateList
import android.graphics.Color
import android.widget.Button
import androidx.core.content.ContextCompat
import com.example.wegitantionindexcounter.R
import views.mapView.MapOverlayHandler

class MarkerAddAvailableBtn(mapOverlayHandler: MapOverlayHandler, btn : Button) : MapBtn(mapOverlayHandler, btn) {

    override fun turnButtonOn() {
        super.turnButtonOn()
        enableMarkers()
    }

    override fun turnButtonOff() {
        super.turnButtonOff()
        disableMarkers()
    }

    private fun enableMarkers() {
        rButton.compoundDrawableTintList = ColorStateList.valueOf(ContextCompat.getColor(rButton.context, R.color.purple_500))
    }

    private fun disableMarkers() {
        rButton.compoundDrawableTintList = ColorStateList.valueOf(Color.argb(100,0,0,0))
    }
}