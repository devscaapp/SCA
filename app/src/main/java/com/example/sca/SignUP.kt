package com.example.sca

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignUP : AppCompatActivity() {

     lateinit var image_view_background : ImageView
    lateinit var text_view_login: TextView
   // lateinit var edit_text_full_name: EditText
    lateinit var edit_text_email: EditText
    lateinit var edit_text_phone_number: EditText
    lateinit var edit_Text_passowrd: EditText
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        image_view_background= findViewById(R.id.imageViewBackground)
        text_view_login= findViewById(R.id.textViewLogin)
        //edit_text_full_name= findViewById(R.id.editTextFullName)
        edit_text_email= findViewById(R.id.editTextEmail)
        edit_text_phone_number= findViewById(R.id.editTextPhoneNumber)
        var button_register: Button= findViewById(R.id.buttonRegister)
        edit_Text_passowrd = findViewById(R.id.editTextPassowrd)
        val edit_text_full_name: EditText = findViewById(R.id.editTextFullName)


        // Initialize Firebase Auth
        auth = Firebase.auth

        //var name = edit_text_full_name.text.toString()

        button_register.setOnClickListener{
            var email= edit_text_email.text.toString().trim()
            var pass = edit_Text_passowrd.text.toString().trim()
            auth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "createUserWithEmail:success")
                        val user = auth.currentUser
                        updateUI(user)
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", task.exception)
                        Toast.makeText(
                            baseContext,
                            "Authentication failed.",
                            Toast.LENGTH_SHORT,
                        ).show()
                        updateUI(null)
                    }
                }

        }
        text_view_login.setOnClickListener{
            val intent = Intent(this,sign_in::class.java)
            startActivity(intent)
        }


    }

    private fun updateUI(user: FirebaseUser?) {
        val intent = Intent(this,MainActivity::class.java)
        startActivity(intent)
    }
}