package com.example.sca

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class sign_in : AppCompatActivity() {
    lateinit var text_view_login: TextView
    lateinit var edit_text_email: EditText
    lateinit var edit_Text_passowrd: EditText
    private lateinit var auth: FirebaseAuth



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        val submitButton: Button = findViewById(R.id.buttonRegister)
        edit_text_email= findViewById(R.id.editTextEmail)
        edit_Text_passowrd = findViewById(R.id.editTextPassowrd)
        text_view_login= findViewById(R.id.textRegister)


        auth = Firebase.auth


        text_view_login.setOnClickListener{
            val intent = Intent(this,SignUP::class.java)
            startActivity(intent)
        }
        submitButton.setOnClickListener {

            var email= edit_text_email.text.toString().trim()
            var pass = edit_Text_passowrd.text.toString().trim()

            auth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information

                        val verification = auth.currentUser?.isEmailVerified
                        if(verification==true)
                        {
                            Log.d(TAG, "signInWithEmail:success")
                            val user = auth.currentUser
                            updateUI(user)
                        }
                        else
                        {
                            Toast.makeText(baseContext,"Please verify your Email",Toast.LENGTH_SHORT, ).show()
                        }
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.exception)
                        Toast.makeText(
                            baseContext,
                            "Authentication failed.",
                            Toast.LENGTH_SHORT,
                        ).show()
                        updateUI(null)
                    }
                }

        }


    }
    private fun updateUI(user: FirebaseUser?) {
        Toast.makeText(baseContext,"Login Successful.",Toast.LENGTH_SHORT,).show()
        val intent = Intent(this,MainActivity::class.java)
        startActivity(intent)
    }
    private fun updateUI() {
        Toast.makeText(baseContext,"Login Failed",Toast.LENGTH_SHORT,).show()
        val intent = Intent(this,Register::class.java)
        startActivity(intent)
    }
    public override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            updateUI(currentUser)
        }
    }
}