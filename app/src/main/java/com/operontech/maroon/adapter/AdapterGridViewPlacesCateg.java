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

public class AdapterGridViewPlacesCateg extends BaseAdapter {

	private final Context mContext;

	private final String[] CATEG_STRINGS = { "Resident Hall", "Food & Dining", "Academic", "Athletic", "Support", "Other" };
	private final int[] CATEG_DRAWABLES = { R.drawable.ic_action_home, R.drawable.ic_dining, R.drawable.ic_academic, R.drawable.ic_athletic, R.drawable.ic_help_outline, R.drawable.ic_other };

	public AdapterGridViewPlacesCateg(final Context context) {
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
			imageView.setImageResource(CATEG_DRAWABLES[i]);
			textView.setText(CATEG_STRINGS[i]);
		} else {
			gridView = convertView;
		}
		return gridView;
	}

	@Override
	public int getCount() {
		return CATEG_STRINGS.length;
	}

	@Override
	public Object getItem(final int i) {
		return CATEG_STRINGS[i];
	}

	@Override
	public long getItemId(final int i) {
		return CATEG_DRAWABLES[i];
	}
}
