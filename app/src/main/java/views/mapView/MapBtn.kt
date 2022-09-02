package com.example.wegitantionindexcounter

import android.content.res.ColorStateList
import android.graphics.Color
import android.widget.Button
import androidx.core.content.ContextCompat
import views.mapView.MapOverlayHandler

open class MapBtn(
    protected val mapOverlayHandler : MapOverlayHandler,
    protected val rButton : Button) {

    var isEnabled = false

    open fun pressButton() {}
}
class RotateMapBtn(_mapOverlayHandler: MapOverlayHandler, _btn : Button) : MapBtn(_mapOverlayHandler, _btn) {
    override fun pressButton() {
        if(isEnabled) {
            disableMapRotate()
            isEnabled = false
        }
        else {
            enableMapRotate()
            isEnabled = true
        }
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
class MarkerAddAvailableBtn(_mapOverlayHandler: MapOverlayHandler, _btn : Button) : MapBtn(_mapOverlayHandler, _btn) {
    override fun pressButton() {
        if(isEnabled) {
            disableMarkers()
            isEnabled = false
        }
        else {
            enableMarkers()
            isEnabled = true
        }
    }
    private fun enableMarkers() {
        rButton.compoundDrawableTintList = ColorStateList.valueOf(ContextCompat.getColor(rButton.context, R.color.purple_500))
    }
    private fun disableMarkers() {
        rButton.compoundDrawableTintList = ColorStateList.valueOf(Color.argb(100,0,0,0))
    }
}