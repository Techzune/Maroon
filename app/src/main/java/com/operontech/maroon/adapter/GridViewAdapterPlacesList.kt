package com.operontech.maroon.adapter

import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.operontech.maroon.R
import com.operontech.maroon.db.PlaceListing
import com.operontech.maroon.frag.FragmentMap


class GridViewAdapterPlacesList(val placeListings: MutableList<PlaceListing>, val fragManager: FragmentManager) : RecyclerView.Adapter<GridViewAdapterPlacesList.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent!!.context).inflate(R.layout.item_recyclerview_placelisting, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return placeListings.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val listing = placeListings[position]
        holder.placeName.text = listing.title
        holder.placeDescription.text = listing.description

    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        val placeName: TextView = view.findViewById(R.id.placelisting_text_title)
        val placeDescription: TextView = view.findViewById(R.id.placelisting_text_description)

        init {
            placeName.setOnClickListener(this)
            placeDescription.setOnClickListener(this)
        }

        override fun onClick(view: View?) {
            val listing = placeListings[adapterPosition]
            val frag = FragmentMap()
            val bundle = Bundle()
            bundle.putSerializable("target", listing)
            frag.arguments = bundle
            fragManager.run {
                beginTransaction()
                        .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                        .replace(R.id.main_fragment, frag)
                        .addToBackStack(null)
                        .commit()
            }
        }
    }
}
