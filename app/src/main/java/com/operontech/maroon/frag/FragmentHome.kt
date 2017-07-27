package com.operontech.maroon.frag

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.operontech.maroon.R
import java.util.*

class FragmentHome : Fragment() {

    @BindView(R.id.home_text_welcome)
    lateinit var welcomeText: TextView

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_home, container, false)
        
        // ButterKnife Binding
        ButterKnife.bind(this, view)

        // Update the front screen text to display time
        updateWelcomeText()

        return view
    }

    override fun onResume() {
        super.onResume()
        activity.setTitle(R.string.title_home)
    }

    /**
     * Displays the appropriate text for the current hour of the day.
     */
    private fun updateWelcomeText() {
        val curHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        when (curHour) {
            in 0..11 -> welcomeText.setText(R.string.good_morning)
            in 12..16 -> welcomeText.setText(R.string.good_afternoon)
            in 16..24 -> welcomeText.setText(R.string.good_evening)
            else -> welcomeText.setText(R.string.hello_there)
        }
    }
}
