package com.operontech.maroon.frag;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import com.operontech.maroon.R;

public class FragmentPlacesCategories extends Fragment {

	GridView gridView;

	@Nullable
	@Override
	public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, final Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.fragment_places_categories, container, false);

		gridView = (GridView) view.findViewById(R.id.places_categories_gridview);

		return view;
	}
}
