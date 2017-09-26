package com.operontech.maroon.frag

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.BindView
import butterknife.ButterKnife
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.operontech.maroon.R
import com.operontech.maroon.adapter.GridViewAdapterPlacesList
import com.operontech.maroon.db.PlaceListing


class FragmentPlacesList : Fragment() {

    @BindView(R.id.places_list_recyclerview)
    lateinit var recyclerView: RecyclerView

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_places_list, container, false)
        ButterKnife.bind(this, view)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)
        setupAdapter()
        return view
    }

    override fun onResume() {
        super.onResume()
        activity.title = getString(R.string.title_placesList, arguments.getString("typeName"))
    }

    private fun setupAdapter() {
        val dataRef = FirebaseDatabase.getInstance().reference.child("data").child(arguments.getString("typeID"))
        dataRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val listings: MutableList<PlaceListing> = ArrayList()

                for (child in dataSnapshot.children) {
                    val listing = child.getValue(PlaceListing::class.java)
                    if (listing != null) {
                        listing.id = child.key
                        listings.add(listing)
                    }
                }

                recyclerView.adapter = GridViewAdapterPlacesList(listings)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("The read failed: " + databaseError.code)
            }
        })
    }
}
