package com.operontech.maroon.frag

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.GridView
import butterknife.BindView
import butterknife.ButterKnife
import com.operontech.maroon.R
import com.operontech.maroon.activity.ActivityMain
import com.operontech.maroon.adapter.GridViewAdapterPlacesType
import com.operontech.maroon.db.PlaceType

class FragmentPlacesTypes : Fragment() {

    @BindView(R.id.places_types_gridview)
    lateinit var gridView: GridView

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_places_types, container, false)
        ButterKnife.bind(this, view)
        gridView.adapter = GridViewAdapterPlacesType(activity)
        gridView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val item = gridView.getItemAtPosition(position) as PlaceType
            val bundle = Bundle()
            bundle.putString("type", item.title)

            val listFragment = FragmentPlacesList()
            listFragment.arguments = bundle
            (activity as ActivityMain).showFragment(listFragment)
        }
        return view
    }

    override fun onResume() {
        super.onResume()
        activity.title = "Places - Categories"
    }
}
