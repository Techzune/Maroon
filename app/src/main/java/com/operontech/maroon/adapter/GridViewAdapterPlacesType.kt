package com.operontech.maroon.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.operontech.maroon.R
import com.operontech.maroon.db.PlaceType

class GridViewAdapterPlacesType(private val mContext: Context) : BaseAdapter() {
    private val placeTypes = PlaceType.values()

    @SuppressLint("InflateParams")
    override fun getView(position: Int, convertView: View?, viewGroup: ViewGroup): View {
        val inflater = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val gridView: View

        if (convertView == null) {
            gridView = inflater.inflate(R.layout.item_gridview_placestypes, null)

            val imageView = gridView.findViewById<ImageView>(R.id.item_gridview_placetype_image)
            val textView = gridView.findViewById<TextView>(R.id.item_gridview_placetype_text)

            val curPlace = placeTypes[position]
            imageView.setImageResource(curPlace.icon)
            textView.text = curPlace.title
        } else {
            gridView = convertView
        }
        return gridView
    }

    override fun getCount(): Int = placeTypes.size

    override fun getItem(i: Int): Any = placeTypes[i]

    override fun getItemId(i: Int): Long = 0
}
