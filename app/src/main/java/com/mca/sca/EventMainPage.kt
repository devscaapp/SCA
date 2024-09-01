package com.mca.sca

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telecom.Call.Details
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide


class EventMainPage : AppCompatActivity() {

    private lateinit var imageView: ImageView
    private lateinit var tvDetails : TextView
    private lateinit var register: Button
    private lateinit var eventname: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_main_page)

        imageView = findViewById(R.id.imageView)
        tvDetails = findViewById(R.id.tvDetails)
        register= findViewById(R.id.buttonNextFP)
        eventname = findViewById(R.id.eventName)

        val eventName = intent.getStringExtra("eventName")
        val imageUrl = intent.getStringExtra("imageUrl")
        val details = intent.getStringExtra("details")
        val registerlink =intent.getStringExtra("action")

        Glide.with(this).load(imageUrl).into(imageView)
        tvDetails.text = details
        eventname.text= eventName

        register.setOnClickListener {
            registerlink?.let {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(registerlink))
                startActivity(intent)
            }
        }




    }
}