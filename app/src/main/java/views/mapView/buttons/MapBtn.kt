package views.mapView.buttons

import android.widget.Button
import views.mapView.overlays.MapOverlayHandler

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