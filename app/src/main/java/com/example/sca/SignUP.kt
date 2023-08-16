package com.example.sca

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

class SignUP : AppCompatActivity() {

     lateinit var image_view_background : ImageView
    lateinit var text_view_login: TextView
    lateinit var edit_text_full_name: EditText
    lateinit var edit_text_email: EditText
    lateinit var edit_text_phone_number: EditText
    lateinit var button_register: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        image_view_background= findViewById(R.id.imageViewBackground)
        text_view_login= findViewById(R.id.textViewLogin)
        edit_text_full_name= findViewById(R.id.editTextFullName)
        edit_text_email= findViewById(R.id.editTextEmail)
        edit_text_phone_number= findViewById(R.id.editTextPhoneNumber)
        button_register= findViewById(R.id.buttonRegister)

        button_register.setOnClickListener{
            Toast.makeText(this, "clicked Rigister", Toast.LENGTH_SHORT).show()
            val intent = Intent(this,sign_in::class.java)
            startActivity(intent)

        }


    }
}