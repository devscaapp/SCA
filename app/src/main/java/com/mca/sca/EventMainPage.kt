package com.mca.sca

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telecom.Call.Details
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide


class EventMainPage : AppCompatActivity() {

    private lateinit var imageView: ImageView
    private lateinit var tvDetails : TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_main_page)

        imageView = findViewById(R.id.imageView)
        tvDetails = findViewById(R.id.tvDetails)

        val eventName = intent.getStringExtra("eventName")
        val imageUrl = intent.getStringExtra("imageUrl")
        val details = intent.getStringExtra("details")

        Glide.with(this).load(imageUrl).into(imageView)
        tvDetails.text = details


    }
}