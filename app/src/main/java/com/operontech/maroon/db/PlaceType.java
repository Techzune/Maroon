package com.operontech.maroon.db;

import com.operontech.maroon.R;

public enum PlaceType {
	//@formatter:off
	ACADEMIC("Academic", R.drawable.ic_academic),
	ATHLETIC("Athletic", R.drawable.ic_athletic),
	FOODANDDINING("Food & Dining", R.drawable.ic_dining),
	RESIDENTHALL("Residence Hall", R.drawable.ic_action_home),
	OTHER("Other", R.drawable.ic_help_outline),
	ALL("All", R.drawable.ic_all);

	//@formatter:on

	String title;
	int icon;

	PlaceType(final String title, final int icon) {
		this.title = title;
		this.icon = icon;
	}

	public String getTitle() {
		return title;
	}

	public int getIconResource() {
		return icon;
	}
}
