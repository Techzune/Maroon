package com.operontech.maroon.db

import com.operontech.maroon.R

enum class PlaceType(val title: String, val icon: Int) {
    //@formatter:off
	ACADEMIC("Academic", R.drawable.ic_academic),
    ATHLETIC("Athletic", R.drawable.ic_athletic),
    FOODANDDINING("Food & Dining", R.drawable.ic_dining),
    RESIDENCEHALL("Residence Hall", R.drawable.ic_action_home),
    OTHER("Other", R.drawable.ic_help_outline),
    ALL("All", R.drawable.ic_all);
    //@formatter:on
}
