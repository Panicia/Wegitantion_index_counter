package views.mapView.buttons

import android.content.res.ColorStateList
import android.graphics.Color
import android.widget.Button
import androidx.core.content.ContextCompat
import com.example.wegitantionindexcounter.R
import views.mapView.mapOverlays.MapOverlayHandler

class RotateMapBtn (mapOverlayHandler: MapOverlayHandler, btn : Button) : MapBtn(mapOverlayHandler, btn) {

    override fun turnButtonOn() {
        super.turnButtonOn()
        enableMapRotate()
    }

    override fun turnButtonOff() {
        super.turnButtonOff()
        disableMapRotate()
    }

    private fun enableMapRotate() {
        mapOverlayHandler.rotateOn()
        rButton.compoundDrawableTintList = ColorStateList.valueOf(ContextCompat.getColor(rButton.context, R.color.purple_500))
    }

    private fun disableMapRotate() {
        mapOverlayHandler.rotateOff()
        rButton.compoundDrawableTintList = ColorStateList.valueOf(Color.argb(100,0,0,0))
    }
}