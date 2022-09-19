package views.mapView.buttons

import android.content.res.ColorStateList
import android.graphics.Color
import android.widget.Button
import androidx.core.content.ContextCompat
import views.mapView.MapOverlayHandler

open class MapBtn(
    protected val mapOverlayHandler : MapOverlayHandler,
    protected val rButton : Button) {

    var isEnabled = false

    fun pressButton() {
        isEnabled = if(isEnabled) {
            turnButtonOff()
            false
        } else {
            turnButtonOn()
            true
        }
    }

    open fun turnButtonOn() {}

    open fun turnButtonOff() {}
}