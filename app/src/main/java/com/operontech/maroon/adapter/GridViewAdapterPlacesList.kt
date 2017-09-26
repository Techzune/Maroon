package com.operontech.maroon.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.operontech.maroon.R
import com.operontech.maroon.db.PlaceListing


class GridViewAdapterPlacesList(private val placeListings: MutableList<PlaceListing>) : RecyclerView.Adapter<GridViewAdapterPlacesList.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent!!.context).inflate(R.layout.item_recyclerview_placelisting, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return placeListings.size
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        val listingName = placeListings[position].title
        holder!!.placeName.text = listingName
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val placeName: TextView = view.findViewById(R.id.placelisting_text_title)
    }
}
