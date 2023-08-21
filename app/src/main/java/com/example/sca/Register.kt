package com.example.sca

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import kotlin.math.sign

class Register : AppCompatActivity() {

    lateinit var text_view_login: TextView
    lateinit var button_register: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        text_view_login= findViewById(R.id.login_text)
        button_register= findViewById(R.id.registerButton)

        text_view_login.setOnClickListener{
            val intent = Intent(this,sign_in::class.java)
            startActivity(intent)
        }
        button_register.setOnClickListener{
            Toast.makeText(this, "clicked Register", Toast.LENGTH_SHORT).show()
            val intent = Intent(this,SignUP::class.java)
            startActivity(intent)
        }

    }
}