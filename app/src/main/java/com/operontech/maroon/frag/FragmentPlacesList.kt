package com.operontech.maroon.frag

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
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

    @BindView(R.id.places_list_recyclerView)
    lateinit var recyclerView: RecyclerView

    @BindView(R.id.places_list_progressLayout)
    lateinit var progressLayout: RelativeLayout

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_places_list, container, false)
        ButterKnife.bind(this, view)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)
        //recyclerView.setOnClickListener(AdapterView.OnClickListener())
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
                progressLayout.visibility = View.VISIBLE
                val listings: MutableList<PlaceListing> = ArrayList()

                for (child in dataSnapshot.children) {
                    val listing = child.getValue(PlaceListing::class.java)
                    if (listing != null) {
                        listing.id = child.key
                        listings.add(listing)
                    }
                }

                recyclerView.adapter = GridViewAdapterPlacesList(listings)
                progressLayout.visibility = View.GONE
            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("The read failed: " + databaseError.code)
                progressLayout.visibility = View.GONE
            }
        })
    }
}
