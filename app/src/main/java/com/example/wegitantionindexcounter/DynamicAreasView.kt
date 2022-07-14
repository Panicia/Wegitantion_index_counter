package com.example.wegitantionindexcounter

import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polygon

class DynamicAreasView(_bottomSheet: FrameLayout, _listView: LinearLayout, _context: Context) {
    private val bottomSheet = _bottomSheet
    private val listView = _listView
    private val context = _context
    private val viewArray = ArrayList<View>()

    init {
        BottomSheetBehavior.from(bottomSheet).apply {
            peekHeight = 0
            this.state = BottomSheetBehavior.STATE_COLLAPSED
        }
    }
    fun addMarkerInView(marker: Marker) {
        val textView = TextView(context)
        val layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT)
        layoutParams.setMargins(50, 20,50, 20)
        textView.layoutParams = layoutParams
        textView.setTextColor(Color.BLACK)
        textView.textSize = 20F
        textView.text = marker.title
        viewArray.add(textView)
        listView.addView(textView)
        changeCollapsedHeight(280)
    }
    fun addMarkerInView2(marker: Marker) {

    }
    fun addPolygonInView(polygon: Polygon) {

    }
    private fun changeCollapsedHeight(height: Int) {
        BottomSheetBehavior.from(bottomSheet).apply {
            peekHeight = height
        }
    }
    fun hideSheet() {
        BottomSheetBehavior.from(bottomSheet).apply {
            peekHeight = 0
        }
    }
}