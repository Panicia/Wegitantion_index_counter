package com.example.wegitantionindexcounter

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import org.osmdroid.views.overlay.Marker

class MarkerLayout @JvmOverloads constructor(
    context : Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    private val marker : Marker,
    private val listView: LinearLayout

    ) : ConstraintLayout(context, attrs, defStyleAttr) {

    private lateinit var textView1 : TextView
    private lateinit var textView2 : TextView
    private lateinit var imageView1 : ImageView

    init {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        View.inflate(context, R.layout.marker_layout_for_list, this)
        textView1 = findViewById(R.id.markerTextView1)
        textView2 = findViewById(R.id.markerTextView2)
        imageView1 = findViewById(R.id.icon_marker)
        textView1.text = marker.title
        textView2.text = marker.getMarkerPos()
        imageView1.setImageResource(R.drawable.geo_icon_64)
    }

    private fun Marker.getMarkerPos() : String {
        return "lat: ${position.latitude}\nlon: ${position.longitude}"
    }
}