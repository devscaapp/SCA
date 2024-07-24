package com.mca.sca

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignUP : AppCompatActivity() {

    private lateinit var image_view_background: ImageView
    private lateinit var text_view_login: TextView
    private lateinit var edit_text_email: EditText
    private lateinit var edit_text_password: EditText
    private lateinit var edit_text_confirm_password: EditText
    private lateinit var button_register: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        image_view_background = findViewById(R.id.imageViewBackgroundSU)
        text_view_login = findViewById(R.id.textViewLoginSU)
        edit_text_email = findViewById(R.id.editTextEmailSU)
        button_register = findViewById(R.id.buttonRegisterSU)
        edit_text_password = findViewById(R.id.editTextPasswordSU)
        edit_text_confirm_password = findViewById(R.id.editTextConfirmPasswordSU)
        progressBar = findViewById(R.id.progressBar)

        // Initialize Firebase Auth
        auth = Firebase.auth

        button_register.setOnClickListener {
            val email = edit_text_email.text.toString().trim()
            val password = edit_text_password.text.toString().trim()
            val confirmPassword = edit_text_confirm_password.text.toString().trim()

            if (validateInputs(email, password, confirmPassword)) {
                registerUser(email, password)
            }
        }

        text_view_login.setOnClickListener {
            val intent = Intent(this, sign_in::class.java)
            startActivity(intent)
        }

        image_view_background.setOnClickListener {
            val intent = Intent(this, Register::class.java)
            startActivity(intent)
        }
    }

    private fun validateInputs(email: String, password: String, confirmPassword: String): Boolean {
        if (email.isEmpty()) {
            edit_text_email.error = "Email is required"
            edit_text_email.requestFocus()
            return false
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edit_text_email.error = "Please enter a valid email"
            edit_text_email.requestFocus()
            return false
        }

        if (password.isEmpty()) {
            edit_text_password.error = "Password is required"
            edit_text_password.requestFocus()
            return false
        }

        if (password.length < 6) {
            edit_text_password.error = "Password should be at least 6 characters"
            edit_text_password.requestFocus()
            return false
        }

        if (password != confirmPassword) {
            edit_text_confirm_password.error = "Passwords do not match"
            edit_text_confirm_password.requestFocus()
            return false
        }

        return true
    }

    private fun registerUser(email: String, password: String) {
        progressBar.visibility = ProgressBar.VISIBLE
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                progressBar.visibility = ProgressBar.GONE
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    auth.currentUser?.sendEmailVerification()
                        ?.addOnSuccessListener {
                            Toast.makeText(baseContext, "Verify your account and login", Toast.LENGTH_LONG).show()
                            val user = auth.currentUser
                            //updateUI(user)
                        }
                        ?.addOnFailureListener {
                            Toast.makeText(baseContext, "Email Verification Failed", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(baseContext, "Verificaiton link sent please verify and login.", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun updateUI(user: FirebaseUser?) {
        val intent = Intent(this, sign_in::class.java)
        startActivity(intent)
    }
}
