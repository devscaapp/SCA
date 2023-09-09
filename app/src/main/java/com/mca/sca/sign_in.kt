package com.mca.sca

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
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
    lateinit var edit_Text_passowrd: EditText
    private lateinit var auth: FirebaseAuth

    private lateinit var client: GoogleSignInClient




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        val submitButton: Button = findViewById(R.id.buttonRegister)
        edit_text_email= findViewById(R.id.editTextEmail)
        edit_Text_passowrd = findViewById(R.id.editTextPassowrd)
        text_view_login= findViewById(R.id.textRegister)
        val googlebtn: Button = findViewById(R.id.googleButtonx)
        val forgotpassword: TextView = findViewById(R.id.forgetpass_btn)

        forgotpassword.setOnClickListener {
            val intent = Intent(this,forgotpassword::class.java)
            startActivity(intent)
        }


        auth = Firebase.auth

        val  options = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        client = GoogleSignIn.getClient(this,options)


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
        val verification = auth.currentUser?.isEmailVerified
        if (currentUser != null && verification==true) {
            updateUI(currentUser)
        }
    }
}