package com.operontech.maroon.frag

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.BindView
import butterknife.ButterKnife
import com.google.firebase.database.FirebaseDatabase
import com.operontech.maroon.R
import com.operontech.maroon.adapter.GridViewAdapterPlacesList
import com.operontech.maroon.db.PlaceListing

class FragmentPlacesList : Fragment() {

    @BindView(R.id.places_list_recyclerview)
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_places_list, container, false)
        ButterKnife.bind(this, view)
        recyclerView.adapter = GridViewAdapterPlacesList(getList())
        return view
    }

    override fun onResume() {
        super.onResume()
        activity.title = getString(R.string.title_placesList, arguments.getString("typeName"))
    }

    private fun getList(): Array<PlaceListing> {
        var dataRef = FirebaseDatabase.getInstance().getReference("")
        return Array(10, { PlaceListing("0", "test", "test") })
    }
}
