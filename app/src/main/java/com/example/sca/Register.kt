package com.example.sca

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
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
import kotlin.math.sign

class Register : AppCompatActivity() {

    lateinit var text_view_login: TextView
    lateinit var button_register: Button
    private lateinit var auth: FirebaseAuth
    private lateinit var client: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        text_view_login= findViewById(R.id.login_text)
        button_register= findViewById(R.id.registerButton)
        val googlebtn: Button = findViewById(R.id.googleButton)

        auth = Firebase.auth

        val  options = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        client = GoogleSignIn.getClient(this,options)

        text_view_login.setOnClickListener{
            val intent = Intent(this,sign_in::class.java)
            startActivity(intent)
        }
        button_register.setOnClickListener{
            Toast.makeText(this, "clicked Register", Toast.LENGTH_SHORT).show()
            val intent = Intent(this,SignUP::class.java)
            startActivity(intent)
        }
        googlebtn.setOnClickListener {
            val intent = client.signInIntent
            startActivityForResult(intent,10001)
        }

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==10001){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val account = task.getResult(ApiException::class.java)
            val credential = GoogleAuthProvider.getCredential(account.idToken,null)
            FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener{task->
                    if(task.isSuccessful){
                        updateUI(auth.currentUser)
                    }else{
                        Toast.makeText(this,task.exception?.message,Toast.LENGTH_SHORT).show()
                    }

                }
        }
    }
    public override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            updateUI(currentUser)
        }
    }
    private fun updateUI() {
        Toast.makeText(baseContext,"Login Failed",Toast.LENGTH_SHORT,).show()
        val intent = Intent(this,Register::class.java)
        startActivity(intent)
    }
    private fun updateUI(user: FirebaseUser?) {
        Toast.makeText(baseContext,"Login Successful.",Toast.LENGTH_SHORT,).show()
        val intent = Intent(this,MainActivity::class.java)
        startActivity(intent)
    }

}