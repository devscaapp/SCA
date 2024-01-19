package com.mca.sca

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthMissingActivityForRecaptchaException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.mca.sca.MainActivity
import com.mca.sca.R
import com.mca.sca.SignUP

import java.util.concurrent.TimeUnit

class VerificationMethod : AppCompatActivity() {

    lateinit var btn_verify_email: Button
    lateinit var btn_verify_phone: Button
    lateinit var buttonNextVM: Button
    private lateinit var auth: FirebaseAuth
    var phone : String= "+918837041258"
    var flag =0
    lateinit var imageViewBackgroundVM: ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verificaition_method)


        btn_verify_email= findViewById(R.id.btn_verify_email)
        btn_verify_phone= findViewById(R.id.btn_verify_phone)
        buttonNextVM= findViewById(R.id.buttonNextVM)
        imageViewBackgroundVM = findViewById(R.id.imageViewBackgroundVM)

        var email = intent.getStringExtra("email")
        //phone = intent.getStringExtra("phone")
        //phone= "8700689606".toString()


        auth = Firebase.auth

        btn_verify_email.setOnClickListener{
            flag=1

        }

        btn_verify_phone.setOnClickListener{
            flag=2

        }

        buttonNextVM.setOnClickListener{
            if(flag==1)   //user selected email verification
            {

            }
            else if(flag==2)   //user selected phone verification
            {
                if (phone != null) {


                    sendVerificationCode(phone!!)
                }
                    else
                    {
                        Toast.makeText(this, "Please enter correct number", Toast.LENGTH_SHORT).show()
                    }



                flag=0
            }
        }

        imageViewBackgroundVM.setOnClickListener{
            val intent = Intent(this, SignUP::class.java)
            startActivity(intent)
        }


    }

    private fun sendVerificationCode(phone: String) {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phone) // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this) // Activity (for callback binding)
            .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)

    }

    private fun sendToMain() {
        startActivity(Intent(this, MainActivity::class.java))
    }


    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information

                    Toast.makeText(this, "Authenticated successfully", Toast.LENGTH_SHORT).show()
                    sendToMain()
                } else {
                    // Sign in failed, display a message and update the UI
                    Log.d("TAG", "signInWithPhoneAuthCredential: ${task.exception.toString()}")
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                    }
                    // Update UI
                }
            }
    }



    private    val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            // This callback will be invoked in two situations:
            // 1 - Instant verification. In some cases the phone number can be instantly
            //     verified without needing to send or enter a verification code.
            // 2 - Auto-retrieval. On some devices Google Play services can automatically
            //     detect the incoming verification SMS and perform verification without
            //     user action.

            signInWithPhoneAuthCredential(credential)
        }

        override fun onVerificationFailed(e: FirebaseException) {
            // This callback is invoked in an invalid request for verification is made,
            // for instance if the the phone number format is not valid.


            if (e is FirebaseAuthInvalidCredentialsException) {
                // Invalid request
                Log.d("Tag", "onVerificationFailed: ${e.toString()}")
            } else if (e is FirebaseTooManyRequestsException) {
                // The SMS quota for the project has been exceeded
                Log.d("Tag", "onVerificationFailed: ${e.toString()}")
            } else if (e is FirebaseAuthMissingActivityForRecaptchaException) {
                // reCAPTCHA verification attempted with null Activity
                Log.d("Tag", "onVerificationFailed: ${e.toString()}")
            }

            // Show a message and update the UI
        }

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken,
        ) {
            // The SMS verification code has been sent to the provided phone number, we
            // now need to ask the user to enter the code and then construct a credential
            // by combining the code with a verification ID.
            // Save verification ID and resending token so we can use them later
            Log.d(TAG, "onCodeSent:$verificationId")

            val intent  = Intent( this@VerificationMethod, VerifyPhone::class.java)
            //phone = intent.getStringExtra("phone")
            intent.putExtra("OTP", verificationId)
            intent.putExtra("resentToken", token)
            intent.putExtra("phoneNumber", phone)

            startActivity(intent)

        }
    }

    override fun onStart() {
        super.onStart()
        //if user is already authenticated then go to the main activity
        if(auth.currentUser!= null){
            startActivity(Intent(this,MainActivity::class.java ))
        }
    }
}