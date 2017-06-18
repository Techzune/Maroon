package com.operontech.maroon.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import butterknife.BindView
import com.operontech.maroon.R

class ActivityStart : AppCompatActivity() {

    @BindView(R.id.start_button_student)
    lateinit var buttonStudent: Button

    @BindView(R.id.start_button_nonstudent)
    lateinit var buttonNonStudent: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        buttonNonStudent.setOnClickListener {
            val intent = Intent(this@ActivityStart, ActivityStartLast::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

    }
}
