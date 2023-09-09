package com.mca.sca

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class forgotpassword : AppCompatActivity() {
    lateinit var edit_text_email: EditText
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        val next: Button = findViewById(R.id.buttonNextFP)
        edit_text_email= findViewById(R.id.editTextEmailFP)

        auth = Firebase.auth

        next.setOnClickListener{
            var email= edit_text_email.text.toString().trim()
            auth.sendPasswordResetEmail(email)
                .addOnSuccessListener {
                    Toast.makeText(baseContext,"Please check your Email", Toast.LENGTH_SHORT, ).show()
                }
                .addOnFailureListener {
                    Toast.makeText(baseContext,it.toString(), Toast.LENGTH_SHORT, ).show()
                }
            val intent = Intent(this,sign_in::class.java)
            startActivity(intent)
        }

    }
}