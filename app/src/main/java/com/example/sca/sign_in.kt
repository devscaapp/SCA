package com.example.sca

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

class sign_in : AppCompatActivity() {
    lateinit var text_view_login: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)


        val editText: EditText = findViewById(R.id.editTextEmail)
        val submitButton: Button = findViewById(R.id.buttonRegister)

        text_view_login= findViewById(R.id.textRegister)
        text_view_login.setOnClickListener{
            val intent = Intent(this,SignUP::class.java)
            startActivity(intent)
    }
        submitButton.setOnClickListener {

            // get the data with the "editText.text.toString()"
            val enteredData: String = editText.text.toString()

            // check whether the retrieved data is empty or not
            // based on the emptiness provide the Toast Message
            if (enteredData.isEmpty()) {
                Toast.makeText(applicationContext, "Please Enter the Data", Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(applicationContext, enteredData, Toast.LENGTH_SHORT).show()
            }
        }


    }
}