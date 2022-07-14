package com.example.wegitantionindexcounter

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.widget.Button
import androidx.core.content.ContextCompat

open class MapBtn(_mapOverlayHandler : MapOverlayHandler, _btn : Button, _context : Context) {
    protected val context = _context
    protected val rButton = _btn
    protected val mapOverlayHandler = _mapOverlayHandler

    var isEnabled = false
        get() {
            return field
        }
    open fun pressButton() {}
}
class RotateMapBtn(_mapOverlayHandler: MapOverlayHandler, _btn : Button, _context : Context) : MapBtn(_mapOverlayHandler, _btn, _context) {
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
        mapOverlayHandler.setRotate()
        rButton.compoundDrawableTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.purple_500))
    }
    private fun disableMapRotate() {
        mapOverlayHandler.deleteRotate()
        rButton.compoundDrawableTintList = ColorStateList.valueOf(Color.argb(100,0,0,0))
    }
}
class MarkerAddAvailableBtn(_mapOverlayHandler:MapOverlayHandler, _btn : Button, _context : Context) : MapBtn(_mapOverlayHandler, _btn, _context) {
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
        rButton.compoundDrawableTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.purple_500))
    }
    private fun disableMarkers() {
        rButton.compoundDrawableTintList = ColorStateList.valueOf(Color.argb(100,0,0,0))
    }
}