package com.operontech.maroon.frag

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import butterknife.BindView
import com.operontech.maroon.R
import com.operontech.maroon.adapter.GridViewAdapterPlacesType

class FragmentPlacesTypes : Fragment() {

    @BindView(R.id.places_types_gridview)
    lateinit var gridView: GridView

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        gridView.adapter = GridViewAdapterPlacesType(activity)
        return inflater!!.inflate(R.layout.fragment_places_types, container, false)
    }

    override fun onResume() {
        super.onResume()
        activity.title = "Places - Categories"
    }
}
