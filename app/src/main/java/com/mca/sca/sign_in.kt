

package com.mca.sca

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class sign_in : AppCompatActivity() {
    lateinit var text_view_login: TextView
    lateinit var edit_text_email: EditText
    lateinit var edit_text_password: EditText
    private lateinit var auth: FirebaseAuth
    private lateinit var progressBar: ProgressBar
    private lateinit var client: GoogleSignInClient
    private lateinit var backBtn: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        val submitButton: Button = findViewById(R.id.buttonRegister)
        edit_text_email = findViewById(R.id.editTextEmail)
        edit_text_password = findViewById(R.id.editTextPassword)
        text_view_login = findViewById(R.id.textRegister)
        val googlebtn: Button = findViewById(R.id.googleButtonx)
        val forgotpassword: TextView = findViewById(R.id.forgetpass_btn)
        val resendVerificationButton: TextView = findViewById(R.id.buttonResendVerification)
        progressBar = findViewById(R.id.progressBar)
        backBtn= findViewById(R.id.imageViewBackground)

        backBtn.setOnClickListener{
            val intent = Intent(this, Register::class.java)
            startActivity(intent)
        }

        forgotpassword.setOnClickListener {
            val intent = Intent(this, forgotpassword::class.java)
            startActivity(intent)
        }

        auth = Firebase.auth

        val options = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        client = GoogleSignIn.getClient(this, options)

        text_view_login.setOnClickListener {
            val intent = Intent(this, SignUP::class.java)
            startActivity(intent)
        }

        submitButton.setOnClickListener {
            val email = edit_text_email.text.toString().trim()
            val pass = edit_text_password.text.toString().trim()

            if (email.isNotEmpty() && pass.isNotEmpty()) {
                progressBar.visibility = View.VISIBLE
                auth.signInWithEmailAndPassword(email, pass)
                    .addOnCompleteListener(this) { task ->
                        progressBar.visibility = View.GONE
                        if (task.isSuccessful) {
                            val user = auth.currentUser
                            val verification = user?.isEmailVerified
                            if (verification == true) {
                                Log.d(TAG, "signInWithEmail:success")
                                updateUI(user)
                            } else {
                                Toast.makeText(baseContext, "Please verify your Email", Toast.LENGTH_SHORT).show()
                                // Show the Resend Verification Email button if the email is not verified
                                resendVerificationButton.visibility = View.VISIBLE
                            }
                        } else {
                            Log.w(TAG, "signInWithEmail:failure", task.exception)
                            Toast.makeText(baseContext, "Authentication failed.", Toast.LENGTH_SHORT).show()
                            updateUI(null)
                        }
                    }
            } else {
                Toast.makeText(baseContext, "Please enter email and password.", Toast.LENGTH_SHORT).show()
            }
        }

        // Resend Verification Email Button Logic
        resendVerificationButton.setOnClickListener {
            val user = auth.currentUser
            user?.sendEmailVerification()?.addOnSuccessListener {
                Toast.makeText(baseContext, "Verification Email Sent", Toast.LENGTH_SHORT).show()
            }?.addOnFailureListener {
                Toast.makeText(baseContext, "Failed to Send Verification Email", Toast.LENGTH_SHORT).show()
            }
        }

        googlebtn.setOnClickListener {
            val intent = client.signInIntent
            startActivityForResult(intent, 10001)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 10001) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                FirebaseAuth.getInstance().signInWithCredential(credential)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            updateUI(auth.currentUser)
                        } else {
                            Toast.makeText(this, task.exception?.message, Toast.LENGTH_SHORT).show()
                        }
                    }
            } catch (e: ApiException) {
                Log.w(TAG, "Google sign in failed", e)
                Toast.makeText(this, "Google sign in failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            Toast.makeText(baseContext, "Login Successful.", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            Toast.makeText(baseContext, "Login Failed", Toast.LENGTH_SHORT).show()
        }
    }

    public override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null && currentUser.isEmailVerified) {
            updateUI(currentUser)
        }
    }
}
