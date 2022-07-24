package com.example.wegitantionindexcounter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.osmdroid.views.overlay.Marker

class MarkerAdapter(val listener : (Marker) -> Unit): RecyclerView.Adapter<MarkerAdapter.SingleViewHolder>() {
    var items : List<Marker> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SingleViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val markerView = inflater.inflate(R.layout.marker_layout_for_list, parent, false)
        return SingleViewHolder(markerView)
    }

    override fun onBindViewHolder(holder: SingleViewHolder, position: Int) {
        holder.bind(items[position], listener)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun updateData(data:List<Marker>) {
        items = data
        notifyDataSetChanged()
    }

    inner class SingleViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView) {
        val icon = itemView.findViewById<ImageView>(R.id.icon_marker)
        val title = itemView.findViewById<TextView>(R.id.markerTextView1)

        fun bind(item:Marker, listener: (Marker) -> Unit) {
            icon.setImageDrawable(item.icon)
            title.text = item.title
            itemView.setOnClickListener {
                listener.invoke(item)
            }
        }
    }
}
