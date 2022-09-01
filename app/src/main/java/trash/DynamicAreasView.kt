package trash

import android.content.Context
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import org.osmdroid.views.overlay.Marker

class DynamicAreasView(
    private val bottomSheet: FrameLayout,
    private val listView: LinearLayout,
    private val context: Context) {

    private val viewArray = ArrayList<View>()
    //private var markerAdapter: MarkerAdapter = MarkerAdapter()

    init {
        BottomSheetBehavior.from(bottomSheet).apply {
            peekHeight = 0
            this.state = BottomSheetBehavior.STATE_COLLAPSED
        }
    }

    fun addMarkerInView(marker: Marker) : MarkerLayout {
        val markerView = MarkerLayout(context, marker = marker)
        listView.addView(markerView)
        changeCollapsedHeight(300)
        return markerView
    }

    fun updateMarkers(markerList:List<Marker>) {

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