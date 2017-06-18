package com.operontech.maroon.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import butterknife.BindView
import com.operontech.maroon.R

class ActivityStartLast : AppCompatActivity() {

    @BindView(R.id.startLast_button_begin)
    lateinit var finishButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start_last)

        // finishButton will open ActivityMain
        finishButton.setOnClickListener {

            // Create activity, prevent user from using back button
            val newIntent = Intent(this@ActivityStartLast, ActivityMain::class.java)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

            startActivity(newIntent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

            finish()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}
