package com.operontech.maroon.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.operontech.maroon.R;
import com.operontech.maroon.db.PlaceType;

public class GridViewAdapterPlacesType extends BaseAdapter {

	private final Context mContext;
	private final PlaceType[] placeTypes = PlaceType.values();

	public GridViewAdapterPlacesType(final Context context) {
		mContext = context;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(final int i, final View convertView, final ViewGroup viewGroup) {
		final LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final View gridView;

		if (convertView == null) {
			gridView = inflater.inflate(R.layout.item_gridview_placestypes, null);

			final ImageView imageView = (ImageView) gridView.findViewById(R.id.item_gridview_placetype_image);
			final TextView textView = (TextView) gridView.findViewById(R.id.item_gridview_placetype_text);

			final PlaceType curPlace = placeTypes[i];
			imageView.setImageResource(curPlace.getIconResource());
			textView.setText(curPlace.getTitle());
		} else {
			gridView = convertView;
		}
		return gridView;
	}

	@Override
	public int getCount() {
		return placeTypes.length;
	}

	@Override
	public Object getItem(final int i) {
		return placeTypes[i];
	}

	@Override
	public long getItemId(final int i) {
		return 0;
	}
}
