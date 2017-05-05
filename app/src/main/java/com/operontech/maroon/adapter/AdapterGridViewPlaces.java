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

public class AdapterGridViewPlaces extends BaseAdapter {

	private final Context mContext;

	public AdapterGridViewPlaces(final Context context) {
		mContext = context;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(final int i, final View convertView, final ViewGroup viewGroup) {
		final LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final View gridView;

		if (convertView == null) {
			gridView = inflater.inflate(R.layout.item_gridview_placestype, null);

			final ImageView imageView = (ImageView) gridView.findViewById(R.id.item_gridview_placetype_image);
			final TextView textView = (TextView) gridView.findViewById(R.id.item_gridview_placetype_text);
		} else {
			gridView = convertView;
		}
		return gridView;
	}

	@Override
	public int getCount() {
		return 0;
	}

	@Override
	public Object getItem(final int i) {
		return null;
	}

	@Override
	public long getItemId(final int i) {
		return 0;
	}
}
