package com.operontech.maroon.frag;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.operontech.maroon.R;
import com.operontech.maroon.db.PlaceListing;

public class FragmentPlacesCategories extends Fragment {

	GridView gridView;

	@Nullable
	@Override
	public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, final Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.fragment_places_categories, container, false);

		final FirebaseDatabase firebase = FirebaseDatabase.getInstance();
		final DatabaseReference fbRef = firebase.getReference("data/places-cat");

		gridView = (GridView) view.findViewById(R.id.places_categories_gridview);
		gridView.setAdapter(new FirebaseListAdapter<PlaceListing>(getActivity(), PlaceListing.class, R.layout.item_gridview_placestype, fbRef) {
			@Override
			protected void populateView(final View view, final PlaceListing placeListing, final int i) {

			}
		});

		return view;
	}
}
