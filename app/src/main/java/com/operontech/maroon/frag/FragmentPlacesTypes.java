package com.operontech.maroon.frag;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import com.operontech.maroon.R;
import com.operontech.maroon.adapter.GridViewAdapterPlacesType;

public class FragmentPlacesTypes extends Fragment {

	GridView gridView;

	@Nullable
	@Override
	public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, final Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.fragment_places_types, container, false);
		gridView = (GridView) view.findViewById(R.id.places_types_gridview);

		final GridViewAdapterPlacesType gridViewAdapter = new GridViewAdapterPlacesType(getActivity());
		gridView.setAdapter(gridViewAdapter);

		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
		getActivity().setTitle("Places - Categories");
	}
}
